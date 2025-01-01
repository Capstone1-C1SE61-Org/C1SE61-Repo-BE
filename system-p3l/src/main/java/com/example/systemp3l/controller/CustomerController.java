package com.example.systemp3l.controller;

import com.example.systemp3l.dto.customer.CustomerInfo;
import com.example.systemp3l.dto.customer.CustomerUserDetailDto;
import com.example.systemp3l.dto.response.MessageResponse;
import com.example.systemp3l.model.Customer;
import com.example.systemp3l.service.ICustomerService;
import com.example.systemp3l.utils.ConverterMaxCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Customer>> findAllCustomer(@RequestParam(value = "name", required = false) Optional<String> name,
                                                          @RequestParam(value = "address", required = false) Optional<String> address,
                                                          @RequestParam(value = "phone", required = false) Optional<String> phone,
                                                          @RequestParam("page") Optional<Integer> page,
                                                          @RequestParam("size") Optional<Integer> size,
                                                          @RequestParam("sort") Optional<String> sort) {
        String searchName = name.orElse("");
        String searchAddress = address.orElse("");
        String searchPhone = phone.orElse("");
        int pages = page.orElse(1);
        int pageSize = size.orElse(5);
        String sortName = sort.orElse("customer_name");
        Page<Customer> searchCustomer = customerService.searchCustomers(searchName, searchAddress, searchPhone,
                PageRequest.of(pages - 1, pageSize, Sort.by(sortName)));
        if (searchCustomer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(searchCustomer, HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveCustomer(@Valid @RequestBody CustomerInfo customerInfo, BindingResult bindingResult) {
        customerInfo.validate(customerInfo, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> {
                        String fieldName = error.getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                    });
            return ResponseEntity.badRequest().body(errors);
        }

        Customer customerLimit = customerService.customerLimit();
        customerInfo.setCustomerCode(ConverterMaxCode.generateNextId(customerLimit.getCustomerCode()));

        try {
            customerService.saveCustomer(customerInfo);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create customer: " + e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) {
        return new ResponseEntity<>(customerService.findById(customerId), HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @PathVariable Integer id, @RequestBody CustomerInfo customerInfo,
                                            BindingResult bindingResult) {
        new CustomerInfo().validate(customerInfo, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        } else {
            customerService.updateCustomer(customerInfo, id);
        }

        return new ResponseEntity<>(new MessageResponse("The customer has successfully edited!"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        try {
            customerService.deleteById(id);
            return new ResponseEntity<>(new MessageResponse("Customer deleted successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Failed to delete customer: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerUserDetailDto> getDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CustomerUserDetailDto customerUserDetailDto = customerService.findUserDetailByUsername(username);

        if (customerUserDetailDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customerUserDetailDto, HttpStatus.OK);
    }
}
