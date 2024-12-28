package com.example.systemp3l.repository;

import com.example.systemp3l.model.Enrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Repository
@Transactional
public interface IEnrollmentRepository extends JpaRepository<Enrollments, Integer> {

    @Query(value = "SELECT COUNT(*) FROM course_customer WHERE course_id = ?1 AND customer_id = ?2", nativeQuery = true)
    Long countCustomerEnrollments(Integer courseId, Integer customerId);

    @Modifying
    @Query(value = "INSERT INTO enrollments (course_id, customer_id, enrollment_day, status) VALUES (?1, ?2, ?3, ?4)",
            nativeQuery = true)
    void saveEnrollment(Integer courseId, Integer customerId, Date enrollmentDay, Boolean status);
}
