package com.example.systemp3l.service.impl;

import com.example.systemp3l.dto.customer.CustomerUserDetailDto;
import com.example.systemp3l.model.Customer;
import com.example.systemp3l.repository.ICustomerRepository;
import com.example.systemp3l.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

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

    @Override
    @Transactional
    public CustomerUserDetailDto findUserDetailByUsername(String username) {
        Tuple tuple = customerRepository.findUserDetailByUsername(username).orElse(null);

        if (tuple != null) {
            return CustomerUserDetailDto.TupleToCustomerDto(tuple);
        }

        return null;
    }
}
