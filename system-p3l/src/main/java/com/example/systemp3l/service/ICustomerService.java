package com.example.systemp3l.service;

import com.example.systemp3l.dto.customer.CustomerUserDetailDto;
import com.example.systemp3l.model.Customer;

public interface ICustomerService {
    void save(Customer customer);

    Customer customerLimit();

    CustomerUserDetailDto findUserDetailByUsername(String username);

    Customer findCustomerByUsername(String username);
}
