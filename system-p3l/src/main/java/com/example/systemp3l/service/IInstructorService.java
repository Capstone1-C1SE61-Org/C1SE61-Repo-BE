package com.example.systemp3l.service;

import com.example.systemp3l.dto.Instructor.InstructorUserDetailDto;
import com.example.systemp3l.model.Instructor;

public interface IInstructorService {
    void save(Instructor instructor);

    Instructor instructorLimit();

    InstructorUserDetailDto findUserDetailByUsername(String username);
}
