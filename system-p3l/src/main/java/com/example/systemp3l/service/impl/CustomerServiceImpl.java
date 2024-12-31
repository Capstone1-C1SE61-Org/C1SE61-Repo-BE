package com.example.systemp3l.service.impl;

import com.example.systemp3l.dto.customer.CustomerInfo;
import com.example.systemp3l.dto.customer.CustomerUserDetailDto;
import com.example.systemp3l.model.Cart;
import com.example.systemp3l.model.Customer;
import com.example.systemp3l.repository.IAccountRepository;
import com.example.systemp3l.repository.ICartRepository;
import com.example.systemp3l.repository.ICustomerRepository;
import com.example.systemp3l.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @Override
    public Page<Customer> searchCustomers(String name, String address, String phone, Pageable pageable) {
        return customerRepository.searchCustomer(name, address, phone, pageable);
    }

    @Override
    public void saveCustomer(CustomerInfo customerInfo) {
        cartRepository.insertCart(customerInfo.getCustomerName(), customerInfo.getCustomerAddress(),
                customerInfo.getCustomerPhone(), customerInfo.getCustomerEmail());
        Optional<Cart> optionalCart = cartRepository.findLastCart();
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            customerRepository.insertCustomer(
                    customerInfo.getCustomerName(),
                    customerInfo.getCustomerEmail(),
                    customerInfo.getCustomerAddress(),
                    customerInfo.getCustomerCode(),
                    customerInfo.getCustomerGender(),
                    customerInfo.getCustomerImg(),
                    customerInfo.getCustomerPhone(),
                    customerInfo.getDateOfBirth(),
                    true,
                    cart.getCartId()
            );
        } else {
            throw new RuntimeException("Failed to create customer. Cart not found.");
        }
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(CustomerInfo customerInfo, Integer id) {
        Integer accountId = customerInfo.getAccount() != null ? customerInfo.getAccount().getAccountId() : null;
        Integer cartId = customerInfo.getCart() != null ? customerInfo.getCart().getCartId() : null;

        customerRepository.updateCustomer(id, customerInfo.getCustomerCode(), customerInfo.getCustomerName(),
                customerInfo.getCustomerEmail(), customerInfo.getCustomerPhone(), customerInfo.getCustomerGender(),
                customerInfo.getDateOfBirth(), customerInfo.getCustomerAddress(), customerInfo.getCustomerImg(),
                true, accountId, cartId);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteCustomerId(id);
    }

    @Override
    public Customer findById(Integer customerId) {
        return customerRepository.findById(customerId).orElse(null);
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

    @Override
    public Customer findCustomerByUsername(String username) {
        return customerRepository.findCustomerByUsername(username);
    }
}
