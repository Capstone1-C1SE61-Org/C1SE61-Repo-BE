package com.example.systemp3l.service.impl;

import com.example.systemp3l.Exception.ResourceNotFoundException;
import com.example.systemp3l.model.Course;
import com.example.systemp3l.repository.ICourseRepository;
import com.example.systemp3l.service.ICourseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class CourseServiceImpl {

    @Autowired
    private ICourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Integer id, Course courseDetails) {
        Course course = getCourseById(id);
        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setCoursePrice(courseDetails.getCoursePrice());
        course.setImage(courseDetails.getImage());
        return courseRepository.save(course);
    }

    public void deleteCourse(Integer id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
}
