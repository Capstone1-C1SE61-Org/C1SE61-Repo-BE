package com.example.systemp3l.service;

import com.example.systemp3l.model.Payment;

public interface IPaymentService {
    Payment update(Payment payment);

    Payment findPaymentByTnxRef(String tnxRef);

    void deleteByTnxRef(String tnxRef);

    Payment findPaymentByCartAndCourse(Integer cartId, Integer courseId);
}
