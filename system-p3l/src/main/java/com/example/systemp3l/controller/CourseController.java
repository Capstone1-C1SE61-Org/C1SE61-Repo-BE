package com.example.systemp3l.controller;

import com.example.systemp3l.dto.course.CourseDTO;
import com.example.systemp3l.model.Course;
import com.example.systemp3l.model.Customer;
import com.example.systemp3l.model.Enrollments;
import com.example.systemp3l.model.Payment;
import com.example.systemp3l.service.ICourseService;
import com.example.systemp3l.service.ICustomerService;
import com.example.systemp3l.service.IEnrollmentService;
import com.example.systemp3l.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IEnrollmentService enrollmentService;

    @Autowired
    private IPaymentService paymentService;

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

    @PutMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<String> updateCourse(@PathVariable("courseId") Integer courseId,
                                                  @RequestBody CourseDTO updatedCourseDTO) {
        try {
            courseService.updateCourse(courseId, updatedCourseDTO);
            return new ResponseEntity<>("The course has been updated successfully.", HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<String> deleteCourse(@PathVariable("courseId") Integer courseId) {
        try {
            courseService.deleteCourse(courseId);
            return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register/{courseId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> registerCourse(@PathVariable("courseId") Integer courseId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Customer customer = customerService.findCustomerByUsername(username);

            if (customer == null) {
                return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            }

            Course course = courseService.findCourseById(courseId);
            if (course == null || !course.getStatus()) {
                return new ResponseEntity<>("Course not available or inactive", HttpStatus.NOT_FOUND);
            }

            boolean isAlreadyEnrolled = enrollmentService.isStudentEnrolled(courseId, customer.getCustomerId());
            if (isAlreadyEnrolled) {
                return new ResponseEntity<>("Student is already registered for this course", HttpStatus.CONFLICT);
            }

            Enrollments enrollment = new Enrollments();
            enrollment.setCourse(course);
            enrollment.setCustomer(customer);
            enrollment.setEnrollmentDay(new Date(System.currentTimeMillis()));
            enrollment.setStatus(true);

            if (course.getCoursePrice() != 0) {
                Payment payment = paymentService.findPaymentByCartAndCourse(customer.getCart().getCartId(), courseId);
                if (payment == null || !payment.isPaid()) {
                    return new ResponseEntity<>("Payment required for this course", HttpStatus.PAYMENT_REQUIRED);
                }
            }
            enrollmentService.saveEnrollment(enrollment);

            course.getCustomers().add(customer);
            customer.getCourses().add(course);

            customerService.save(customer);
            courseService.save(course);
            return new ResponseEntity<>("Student successfully registered for the course", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error during registration: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
