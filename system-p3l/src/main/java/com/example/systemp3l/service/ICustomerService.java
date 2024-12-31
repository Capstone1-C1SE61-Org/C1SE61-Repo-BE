package com.example.systemp3l.service;

import com.example.systemp3l.dto.customer.CustomerInfo;
import com.example.systemp3l.dto.customer.CustomerUserDetailDto;
import com.example.systemp3l.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    Page<Customer> searchCustomers(String name, String address, String phone, Pageable pageable);

    void saveCustomer(CustomerInfo customerInfo);

    void save(Customer customer);

    void updateCustomer(CustomerInfo customerInfo, Integer id);

    void deleteById(Integer id);

    Customer findById(Integer customerId);

    Customer customerLimit();

    CustomerUserDetailDto findUserDetailByUsername(String username);

    Customer findCustomerByUsername(String username);
}
