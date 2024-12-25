package com.example.systemp3l.controller;

import com.example.systemp3l.dto.course.CourseDTO;
import com.example.systemp3l.model.Course;
import com.example.systemp3l.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @GetMapping("")
    public ResponseEntity<List<CourseDTO>> getAllCourse() {
        List<CourseDTO> courseList = courseService.findAll();
        if (courseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Integer id) {
        return new ResponseEntity<>(courseService.findCourseById(id), HttpStatus.OK);
    }

    @GetMapping("/paid")
    public ResponseEntity<List<CourseDTO>> getPaidCourses() {
        List<CourseDTO> paidCourses = courseService.getPaidCourses();
        return new ResponseEntity<>(paidCourses, HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<List<CourseDTO>> getFreeCourses() {
        List<CourseDTO> freeCourses = courseService.getFreeCourses();
        return new ResponseEntity<>(freeCourses, HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam("courseName") String courseName) {
        return courseService.searchCoursesByName(courseName);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

}
