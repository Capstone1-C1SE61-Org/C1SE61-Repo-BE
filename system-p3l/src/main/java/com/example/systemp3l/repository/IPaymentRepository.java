package com.example.systemp3l.repository;

import com.example.systemp3l.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IPaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findPaymentByTnxRef(String tnxRef);

    @Modifying
    @Query(value = "DELETE FROM `payment_cart_details` WHERE payment_id = :id", nativeQuery = true)
    void deleteDetailsByPaymentId(@Param("id") Integer id);

    @Query(value = "SELECT p.* FROM payment p " +
            "JOIN payment_cart_details pcd ON p.id = pcd.payment_id " +
            "JOIN cart_detail cd ON pcd.cart_details_cart_detail_id = cd.cart_detail_id " +
            "WHERE cd.cart_id = ?1 AND cd.course_id = ?2 AND p.is_paid = true", nativeQuery = true)
    Payment findPaymentByCartAndCourse(Integer cartId, Integer courseId);
}
