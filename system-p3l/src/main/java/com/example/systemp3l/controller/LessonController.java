package com.example.systemp3l.controller;

import com.example.systemp3l.dto.lesson.LessonDTO;
import com.example.systemp3l.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    @Autowired
    private ILessonService lessonService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonDTO>> getLessonsByCourseId(@PathVariable("courseId") Integer courseId) {
        List<LessonDTO> lessons = lessonService.getLessonsByCourseId(courseId);
        if (lessons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }
}
