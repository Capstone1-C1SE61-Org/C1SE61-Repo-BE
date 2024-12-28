package com.example.systemp3l.service;

import com.example.systemp3l.model.Cart;
import com.example.systemp3l.model.CartDetail;

import java.util.List;

public interface IEmailService {
    void emailProcess(Cart cart, Integer totalAmount, List<CartDetail> details);
}
