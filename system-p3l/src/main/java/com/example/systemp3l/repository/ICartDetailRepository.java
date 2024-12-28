package com.example.systemp3l.repository;

import com.example.systemp3l.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ICartDetailRepository extends JpaRepository<CartDetail, Integer> {

    @Query(value = "SELECT * FROM cart_detail WHERE cart_id = :id AND status = false", nativeQuery = true)
    List<CartDetail> findByCartId(@Param("id") Integer id);

    @Query(value = "SELECT * FROM cart_detail WHERE course_id = :course_id AND cart_id = :cart_id AND status = false",
            nativeQuery = true)
    Optional<CartDetail> checkAvailable(@Param("course_id") Integer id, @Param("cart_id") Integer cart_id);

    @Modifying
    @Query(value = "INSERT INTO cart_detail (course_id, status, cart_id) " +
            "VALUES(:course_id, false, :cart_id)",
            nativeQuery = true)
    void insertCartDetail(@Param("course_id") Integer course_id, @Param("cart_id") Integer cart_id);

    @Modifying
    @Query(value = "UPDATE cart_detail SET course_id = :course_id, status = :status, cart_id = :cart_id " +
            "WHERE cart_detail_id = :cart_detail_id",
            nativeQuery = true)
    void updateCartDetail(@Param("course_id") Integer course_id, @Param("status") boolean status,
                          @Param("cart_id") Integer cart_id, @Param("cart_detail_id") Integer cart_detail_id);

    @Modifying
    @Query(value = "DELETE FROM cart_detail  WHERE cart_detail_id = :id", nativeQuery = true)
    void deleteCourseById(@Param("id") Integer id);
}
