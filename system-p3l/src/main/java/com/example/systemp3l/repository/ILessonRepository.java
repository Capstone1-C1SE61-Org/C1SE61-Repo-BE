package com.example.systemp3l.repository;

import com.example.systemp3l.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ILessonRepository extends JpaRepository<Lesson, Integer> {

     @Query(value = "select * from lesson where course_id = :courseId", nativeQuery = true)
     List<Lesson> findAllByCourseID(@Param("courseId") Integer courseId);
}
