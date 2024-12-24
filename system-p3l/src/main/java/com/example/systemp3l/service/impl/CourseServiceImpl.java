package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.Course;
import com.example.systemp3l.repository.ICourseRepository;
import com.example.systemp3l.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private ICourseRepository courseRepository;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}
