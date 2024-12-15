package com.example.systemp3l.controller;

import com.example.systemp3l.dto.customer.CustomerUserDetailDto;
import com.example.systemp3l.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

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
