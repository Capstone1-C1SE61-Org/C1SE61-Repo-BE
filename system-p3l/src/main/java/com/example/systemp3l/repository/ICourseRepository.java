package com.example.systemp3l.repository;

import com.example.systemp3l.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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

    @Modifying
    @Query(value = "UPDATE course SET course_name = :courseName, course_price = :coursePrice, image = :image, " +
            "status = :status, update_date = CURRENT_DATE, " +
            "instructor_id = CASE WHEN :instructorId IS NOT NULL THEN :instructorId ELSE instructor_id END " +
            "WHERE course_id = :courseId", nativeQuery = true)
    void updateCourse(@Param("courseId") Integer courseId,
                      @Param("courseName") String courseName,
                      @Param("coursePrice") Integer coursePrice,
                      @Param("image") String image,
                      @Param("status") Boolean status,
                      @Param("instructorId") Integer instructorId);

    @Modifying
    @Query(value = "DELETE FROM course WHERE course_id = :courseId", nativeQuery = true)
    void deleteCourseById(@Param("courseId") Integer courseId);
}
