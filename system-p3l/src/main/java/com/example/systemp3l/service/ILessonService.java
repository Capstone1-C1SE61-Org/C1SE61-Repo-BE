package com.example.systemp3l.service;

import com.example.systemp3l.model.Lesson;

import java.util.List;

public interface ILessonService {
    List<Lesson> getAllByCourseID(Integer id);
}
