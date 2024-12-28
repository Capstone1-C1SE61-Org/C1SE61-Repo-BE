package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.CartDetail;
import com.example.systemp3l.repository.ICartDetailRepository;
import com.example.systemp3l.service.ICartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartDetailServiceImpl implements ICartDetailService {

    @Autowired
    private ICartDetailRepository cartDetailRepository;

    @Override
    public List<CartDetail> findByCartId(Integer id) {
        return cartDetailRepository.findByCartId(id);
    }

    @Override
    public CartDetail checkAvailable(Integer course_id, Integer cart_id) {
        return cartDetailRepository.checkAvailable(course_id, cart_id).orElse(null);
    }

    @Override
    public void add(Integer courseId, Integer cartId) {
        cartDetailRepository.insertCartDetail(courseId, cartId);
    }

    @Override
    public CartDetail update(CartDetail cartDetail) {
        if (cartDetail != null) {
            Integer cart_detail_id = cartDetail.getCartDetailId();
            Integer course_id = cartDetail.getCourse().getCourseId();
            boolean status = cartDetail.isStatus();
            Integer cart_id = cartDetail.getCartId();
            if (cart_detail_id != null) {
                cartDetailRepository.updateCartDetail(course_id, status, cart_id, cart_detail_id);
            }
        }
        return cartDetail;
    }

    @Override
    public void deleteById(Integer id) {
        cartDetailRepository.deleteCourseById(id);
    }
}
