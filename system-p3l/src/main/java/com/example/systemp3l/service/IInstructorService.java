package com.example.systemp3l.service;

import com.example.systemp3l.dto.Instructor.InstructorInfo;
import com.example.systemp3l.dto.Instructor.InstructorUserDetailDto;
import com.example.systemp3l.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IInstructorService {
    Page<Instructor> searchInstructors(String name, String address, String phone, Pageable pageable);

    void save(Instructor instructor);

    void updateInstructor(InstructorInfo instructorInfo, Integer id);

    void deleteById(Integer id);

    Instructor findById(Integer instructorId);

    Instructor instructorLimit();

    InstructorUserDetailDto findUserDetailByUsername(String username);
}
