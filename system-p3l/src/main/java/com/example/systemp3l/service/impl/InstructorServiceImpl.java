package com.example.systemp3l.service.impl;

import com.example.systemp3l.dto.Instructor.InstructorInfo;
import com.example.systemp3l.dto.Instructor.InstructorUserDetailDto;
import com.example.systemp3l.dto.customer.CustomerInfo;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.repository.IInstructorRepository;
import com.example.systemp3l.service.IInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.sql.Date;

@Service
public class InstructorServiceImpl implements IInstructorService {

    @Autowired
    private IInstructorRepository instructorRepository;

    @Override
    public Page<Instructor> searchInstructors(String name, String address, String phone, Pageable pageable) {
        return instructorRepository.searchInstructor(name, address, phone, pageable);
    }

    @Override
    public void save(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    @Override
    public void updateInstructor(InstructorInfo instructorInfo, Integer id) {
        Integer accountId = instructorInfo.getAccount() != null ? instructorInfo.getAccount().getAccountId() : null;

        instructorRepository.updateInstructor(id, instructorInfo.getInstructorCode(), instructorInfo.getInstructorName(),
                instructorInfo.getInstructorEmail(), instructorInfo.getInstructorPhone(),
                instructorInfo.getInstructorGender(), instructorInfo.getDateOfBirth(),
                instructorInfo.getInstructorAddress(), instructorInfo.getInstructorImg(), true, accountId);
    }

    @Override
    public void deleteById(Integer id) {
        instructorRepository.deleteInstructorId(id);
    }

    @Override
    public Instructor findById(Integer instructorId) {
        return instructorRepository.findById(instructorId).orElse(null);
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
