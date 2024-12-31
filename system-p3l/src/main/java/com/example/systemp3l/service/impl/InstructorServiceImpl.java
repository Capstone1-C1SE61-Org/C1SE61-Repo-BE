package com.example.systemp3l.service.impl;

import com.example.systemp3l.dto.Instructor.InstructorUserDetailDto;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.repository.IInstructorRepository;
import com.example.systemp3l.service.IInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

@Service
public class InstructorServiceImpl implements IInstructorService {

    @Autowired
    private IInstructorRepository instructorRepository;

    @Override
    public void save(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    @Override
    public Instructor instructorLimit() {
        return instructorRepository.limitInstructor();
    }

    @Override
    @Transactional
    public InstructorUserDetailDto findUserDetailByUsername(String username) {
        Tuple tuple = instructorRepository.findUserDetailByUsername(username).orElse(null);

        if (tuple != null) {
            return InstructorUserDetailDto.TupleToInstructorDto(tuple);
        }

        return null;
    }
}
