package com.example.systemp3l.service;

import com.example.systemp3l.model.Enrollments;

public interface IEnrollmentService {
    boolean isStudentEnrolled(Integer courseId, Integer customerId);

    void saveEnrollment(Enrollments enrollment);
}
