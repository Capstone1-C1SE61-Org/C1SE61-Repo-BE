package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.Cart;
import com.example.systemp3l.repository.ICartRepository;
import com.example.systemp3l.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartRepository cartRepository;

    @Override
    public Cart update(Cart cart) {
        Integer id = cart.getCartId();
        String name = cart.getReceiverName();
        String address = cart.getReceiverAddress();
        String phone = cart.getReceiverPhone();
        String email = cart.getReceiverEmail();
        cartRepository.updateCart(id, name, address, phone, email);
        return cart;
    }

    @Override
    public Cart findByUsername(String username) {
        return cartRepository.findCartByUsername(username).orElse(null);
    }

    @Override
    public Cart findById(Integer id) {
        return cartRepository.findById(id).orElse(null);
    }
}
