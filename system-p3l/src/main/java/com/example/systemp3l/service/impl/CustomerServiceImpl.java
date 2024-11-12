package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.Customer;
import com.example.systemp3l.repository.ICustomerRepository;
import com.example.systemp3l.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Customer customerLimit() {
        return customerRepository.limitCustomer();
    }
}
