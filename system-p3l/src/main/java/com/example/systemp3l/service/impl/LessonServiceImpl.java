package com.example.systemp3l.service.impl;

import com.example.systemp3l.Exception.ResourceNotFoundException;
import com.example.systemp3l.model.Lesson;
import com.example.systemp3l.repository.ILessonRepository;
import com.example.systemp3l.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements ILessonService {

    @Autowired
    private ILessonRepository lessonRepository;

    public List<Lesson> getAllByCourseID(Integer id) {
        return lessonRepository.findAllByCourseID(id);
    }

    public Lesson getLessonById(Integer id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
    }

    public Lesson createLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Integer id, Lesson lessonDetails) {
        Lesson lesson = getLessonById(id);
        lesson.setLessonName(lessonDetails.getLessonName());
        lesson.setLessonContent(lessonDetails.getLessonContent());
        lesson.setCourse(lessonDetails.getCourse());
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Integer id) {
        Lesson lesson = getLessonById(id);
        lessonRepository.delete(lesson);
    }
}
