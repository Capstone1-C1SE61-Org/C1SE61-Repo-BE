package com.example.systemp3l.controller;

import com.example.systemp3l.config.VnPayConfig;
import com.example.systemp3l.dto.cart.CartWithDetail;
import com.example.systemp3l.dto.response.PaymentResponse;
import com.example.systemp3l.model.Cart;
import com.example.systemp3l.model.CartDetail;
import com.example.systemp3l.model.Payment;
import com.example.systemp3l.service.ICartDetailService;
import com.example.systemp3l.service.ICartService;
import com.example.systemp3l.service.IEmailService;
import com.example.systemp3l.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private ICartDetailService cartDetailService;

    @Autowired
    private IEmailService emailService;

    @PutMapping("")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CartWithDetail cartWithDetail) throws UnsupportedEncodingException {
        Cart cart = cartWithDetail.getCart();
        List<CartDetail> cartDetailList = cartWithDetail.getCartDetailList();
        int totalAmount = 0;
        Set<CartDetail> cartDetails = new HashSet<>();
        cartService.update(cart);
        for (CartDetail cartDetail: cartDetailList) {
            if (!cartDetail.isStatus()) {
                totalAmount += cartDetail.getCourse().getCoursePrice();
                cartDetails.add(cartDetail);
            }
        }
        if (totalAmount != 0) {
            String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Amount", totalAmount + "00");
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_OrderInfo", "Thanh toan khoa hoc:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", "topup");
            vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;

            Payment payment = new Payment();
            payment.setCartDetails(cartDetails);
            payment.setTotalAmount(totalAmount);
            payment.setPaid(false);
            payment.setTnxRef(vnp_TxnRef);
            payment.setCartId(cart.getCartId());
            paymentService.update(payment);

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setStatus("OK");
            paymentResponse.setMessage("Successfully");
            paymentResponse.setUrl(paymentUrl);
            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/transaction/{tnxRef}")
    public ResponseEntity<?> transactionChecking(@PathVariable("tnxRef") String tnxRef) {
        Payment payment = paymentService.findPaymentByTnxRef(tnxRef);
        if (!payment.isPaid()) {
            payment.setPaid(true);
            paymentService.update(payment);
            int totalAmount = payment.getTotalAmount();
            Cart cart = cartService.findById(payment.getCartId());
            List<CartDetail> cartDetails = new ArrayList<>(payment.getCartDetails());
            for (CartDetail cartDetail: cartDetails) {
                cartDetail.setStatus(true);
                cartDetailService.update(cartDetail);
            }
            emailService.emailProcess(cart, totalAmount, cartDetails);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/fail/{tnxRef}")
    public ResponseEntity<?> transactionFail(@PathVariable("tnxRef") String tnxRef) {
        paymentService.deleteByTnxRef(tnxRef);
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
