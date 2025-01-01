package com.example.systemp3l.repository;

import com.example.systemp3l.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ILessonRepository extends JpaRepository<Lesson, Integer> {

    @Query(value = "SELECT * FROM lesson l WHERE l.course_id = :courseId", nativeQuery = true)
    List<Lesson> findByCourseId(Integer courseId);

    @Query(value = "SELECT * FROM lesson l WHERE l.lesson_id = :lessonId", nativeQuery = true)
    Lesson findByLessonId(Integer lessonId);
}
