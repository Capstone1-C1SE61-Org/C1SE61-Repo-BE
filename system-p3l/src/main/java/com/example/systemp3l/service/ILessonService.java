package com.example.systemp3l.service;

import com.example.systemp3l.dto.lesson.LessonDTO;

import java.util.List;

public interface ILessonService {
    List<LessonDTO> getLessonsByCourseId(Integer courseId);

    LessonDTO getLessonById(Integer lessonId);

}
