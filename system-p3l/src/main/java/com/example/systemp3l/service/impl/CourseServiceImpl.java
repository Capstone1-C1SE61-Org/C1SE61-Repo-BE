package com.example.systemp3l.service.impl;

import com.example.systemp3l.dto.course.CourseDTO;
import com.example.systemp3l.error.NotFoundById;
import com.example.systemp3l.model.Course;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.repository.ICourseRepository;
import com.example.systemp3l.repository.IInstructorRepository;
import com.example.systemp3l.service.ICourseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IInstructorRepository instructorRepository;

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @SneakyThrows
    @Override
    public Course findCourseById(Integer id) {
        Optional<Course> course = courseRepository.findCourseById(id);
        if (course.isPresent()) {
            return course.get();
        }
        throw new NotFoundById("Could not find any courses with code: " + id);
    }

    @Override
    public List<CourseDTO> getPaidCourses() {
        List<Course> courses = courseRepository.findPaidCourses();
        return courses.stream().map(course -> new CourseDTO(
                course.getCourseId(),
                course.getCourseName(),
                course.getCoursePrice(),
                course.getImage(),
                course.getStatus(),
                course.getCreateDate(),
                course.getUpdateDate(),
                course.getInstructor().getInstructorId()
        )).collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getFreeCourses() {
        List<Course> courses = courseRepository.findFreeCourses();
        return courses.stream().map(course -> new CourseDTO(
                course.getCourseId(),
                course.getCourseName(),
                course.getCoursePrice(),
                course.getImage(),
                course.getStatus(),
                course.getCreateDate(),
                course.getUpdateDate(),
                course.getInstructor().getInstructorId()
        )).collect(Collectors.toList());
    }

    @Override
    public List<Course> searchCoursesByName(String courseName) {
        return courseRepository.findCoursesByName(courseName);
    }

    @Override
    @Transactional
    public Course createCourse(Course course) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getAuthorities().stream().anyMatch(
                role -> role.getAuthority().equals("ROLE_INSTRUCTOR") || role.getAuthority().equals("ROLE_ADMIN"))) {

            if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_INSTRUCTOR"))) {
                Instructor instructor = instructorRepository.findByUsername(userDetails.getUsername());
                if (instructor != null) {
                    course.setInstructor(instructor);
                } else {
                    throw new SecurityException("Instructor not found");
                }
            } else {
                course.setInstructor(null);
            }

            Date currentDate = new Date(System.currentTimeMillis());
            course.setCreateDate(currentDate);
            course.setUpdateDate(currentDate);

            course.setStatus(true);

            return courseRepository.save(course);
        } else {
            throw new SecurityException("You do not have permission to create a course");
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public CourseDTO updateCourse(Integer courseId, CourseDTO updatedCourseDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getAuthorities().stream().anyMatch(
                role -> role.getAuthority().equals("ROLE_INSTRUCTOR") || role.getAuthority().equals("ROLE_ADMIN"))) {
            Optional<Course> existingCourseOptional = courseRepository.findById(courseId);
            if (existingCourseOptional.isPresent()) {
                Course existingCourse = existingCourseOptional.get();

                if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_INSTRUCTOR"))
                        && !existingCourse.getInstructor().getAccount().getUsername().equals(userDetails.getUsername())) {
                    throw new SecurityException("You can only edit courses you created");
                }

                courseRepository.updateCourse(
                        courseId,
                        updatedCourseDTO.getCourseName(),
                        updatedCourseDTO.getCoursePrice(),
                        updatedCourseDTO.getImage(),
                        true,
                        userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))
                                ? updatedCourseDTO.getInstructorId()
                                : existingCourse.getInstructor().getInstructorId()
                );

                Course updatedCourse = courseRepository.findById(courseId)
                        .orElseThrow(() -> new NotFoundById("Course not found"));

                return new CourseDTO(
                        updatedCourse.getCourseId(),
                        updatedCourse.getCourseName(),
                        updatedCourse.getCoursePrice(),
                        updatedCourse.getImage(),
                        updatedCourse.getStatus(),
                        updatedCourse.getCreateDate(),
                        new Date(System.currentTimeMillis()),
                        updatedCourse.getInstructor() != null ? updatedCourse.getInstructor().getInstructorId() : null
                );


            } else {
                throw new NotFoundById("Could not find any courses with code: " + courseId);
            }
        } else {
            throw new SecurityException("You do not have permission to update a course");
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteCourse(Integer courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getAuthorities().stream().anyMatch(
                role -> role.getAuthority().equals("ROLE_INSTRUCTOR") || role.getAuthority().equals("ROLE_ADMIN"))) {

            Optional<Course> existingCourseOptional = courseRepository.findById(courseId);

            if (existingCourseOptional.isPresent()) {
                Course existingCourse = existingCourseOptional.get();

                if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_INSTRUCTOR"))
                        && !existingCourse.getInstructor().getAccount().getUsername().equals(userDetails.getUsername())) {
                    throw new SecurityException("You can only delete courses you created");
                }

                courseRepository.deleteCourseById(courseId);
            } else {
                throw new NotFoundById("Could not find any courses with code: " + courseId);
            }
        } else {
            throw new SecurityException("You do not have permission to delete a course");
        }
    }
}
