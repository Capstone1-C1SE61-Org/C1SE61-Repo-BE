package com.example.systemp3l.service;

import com.example.systemp3l.model.Cart;

public interface ICartService {
    void save(Cart cart);

    Cart update(Cart cart);

    Cart findByUsername(String username);

    Cart findById(Integer id);
}
