package com.example.systemp3l.service.impl;

import com.example.systemp3l.dto.lesson.LessonDTO;
import com.example.systemp3l.model.Lesson;
import com.example.systemp3l.repository.ILessonRepository;
import com.example.systemp3l.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements ILessonService {

    @Autowired
    private ILessonRepository lessonRepository;


    @Override
    public List<LessonDTO> getLessonsByCourseId(Integer courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
        return lessons.stream()
                .map(lesson -> new LessonDTO(
                        lesson.getLessonId(),
                        lesson.getLessonName(),
                        lesson.getLessonContent(),
                        lesson.getVideo(),
                        lesson.getLessonDuration()
                )).collect(Collectors.toList());
    }
}
