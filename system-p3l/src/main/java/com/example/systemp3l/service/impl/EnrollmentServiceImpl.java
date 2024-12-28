package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.Enrollments;
import com.example.systemp3l.repository.IEnrollmentRepository;
import com.example.systemp3l.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EnrollmentServiceImpl implements IEnrollmentService {

    @Autowired
    private IEnrollmentRepository enrollmentRepository;

    @Override
    public boolean isStudentEnrolled(Integer courseId, Integer customerId) {
        Long count = enrollmentRepository.countCustomerEnrollments(courseId, customerId);
        return count != null && count > 0;
    }

    @Override
    @Transactional
    public void saveEnrollment(Enrollments enrollment) {
        enrollmentRepository.saveEnrollment(
                enrollment.getCourse().getCourseId(),
                enrollment.getCustomer().getCustomerId(),
                enrollment.getEnrollmentDay(),
                enrollment.getStatus()
        );
    }
}
