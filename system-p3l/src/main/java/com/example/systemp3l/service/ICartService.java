package com.example.systemp3l.service;

import com.example.systemp3l.model.Cart;

public interface ICartService {
    Cart update(Cart cart);

    Cart findByUsername(String username);

    Cart findById(Integer id);
}
