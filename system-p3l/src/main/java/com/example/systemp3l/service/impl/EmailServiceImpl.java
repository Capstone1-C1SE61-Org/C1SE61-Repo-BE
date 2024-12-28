package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.Cart;
import com.example.systemp3l.model.CartDetail;
import com.example.systemp3l.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String sender;


    @Override
    public void emailProcess(Cart cart, Integer totalAmount, List<CartDetail> details) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                messageHelper.setFrom(sender);
                messageHelper.setTo(cart.getReceiverEmail());
                messageHelper.setSubject("Email xác nhận thanh toán");

                Locale locale = new Locale("vi", "VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                Context context = new Context();
                context.setVariable("receiverName", cart.getReceiverName());
                context.setVariable("receiverAddress", cart.getReceiverAddress());
                context.setVariable("totalAmount", numberFormat.format(totalAmount));
                context.setVariable("cartDetails", details);
                context.setVariable("locale", new Locale("vi", "VN"));
                String content = templateEngine.process("email-template", context);

                messageHelper.setText(content, true);
            }
        };
        javaMailSender.send(preparator);
    }
}
