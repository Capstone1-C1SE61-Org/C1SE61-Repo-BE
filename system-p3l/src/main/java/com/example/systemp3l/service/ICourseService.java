package com.example.systemp3l.service;

import com.example.systemp3l.dto.course.CourseDTO;
import com.example.systemp3l.model.Course;

import java.util.List;

public interface ICourseService {
    List<CourseDTO> findAll();

    Course findCourseById(Integer id);

    List<CourseDTO> getPaidCourses();
    List<CourseDTO> getFreeCourses();

    List<Course> searchCoursesByName(String courseName);

    Course createCourse(Course course);

//    Course updateCourse(Integer courseId, CourseDTO updatedCourseDTO);
}
