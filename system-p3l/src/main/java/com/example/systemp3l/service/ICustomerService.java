package com.example.systemp3l.service;

import com.example.systemp3l.model.Customer;

public interface ICustomerService {
    void save(Customer customer);

    Customer customerLimit();
}
