package com.example.systemp3l.repository;

import com.example.systemp3l.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ICourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "SELECT * FROM course WHERE course_id = :id", nativeQuery = true)
    Optional<Course> findCourseById(@Param("id") Integer id);

    @Query(value = "SELECT * FROM course WHERE course_price > 0 AND status = true", nativeQuery = true)
    List<Course> findPaidCourses();

    @Query(value = "SELECT * FROM course WHERE course_price = 0 AND status = true", nativeQuery = true)
    List<Course> findFreeCourses();

    @Query(value = "SELECT * FROM course WHERE course_name LIKE %:courseName%", nativeQuery = true)
    List<Course> findCoursesByName(@Param("courseName") String courseName);
}
