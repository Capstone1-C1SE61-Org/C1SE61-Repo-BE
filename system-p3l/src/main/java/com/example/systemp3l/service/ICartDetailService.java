package com.example.systemp3l.service;

import com.example.systemp3l.model.CartDetail;

import java.util.List;

public interface ICartDetailService {
    List<CartDetail> findByCartId(Integer id);

    CartDetail checkAvailable(Integer course_id, Integer cart_id);

    void add(Integer courseId, Integer cartId);

    CartDetail update(CartDetail cartDetail);

    void deleteById(Integer id);
}
