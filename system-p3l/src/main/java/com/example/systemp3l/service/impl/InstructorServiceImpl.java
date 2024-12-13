package com.example.systemp3l.service.impl;

import com.example.systemp3l.Exception.ResourceNotFoundException;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.repository.IInstructorRepository;
import com.example.systemp3l.service.IInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements IInstructorService{

    @Autowired
    private IInstructorRepository instructorRepository;

    /**
     * Get all instructors
     *
     * @return List of instructors
     */
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    /**
     * Get an instructor by ID
     *
     * @param id Instructor ID
     * @return Instructor object
     * @throws ResourceNotFoundException if instructor not found
     */
    public Instructor getInstructorById(int id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with ID: " + id));
    }

    /**
     * Create a new instructor
     *
     * @param instructor Instructor object
     * @return Created instructor
     */
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    /**
     * Update an existing instructor
     *
     * @param id         Instructor ID
     * @param instructor Updated instructor object
     * @return Updated instructor
     * @throws ResourceNotFoundException if instructor not found
     */
    public Instructor updateInstructor(int id, Instructor instructor) {
        Instructor existingInstructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with ID: " + id));

        existingInstructor.setInstructorName(instructor.getInstructorName());
        existingInstructor.setInstructorEmail(instructor.getInstructorEmail());
        existingInstructor.setInstructorPhone(instructor.getInstructorPhone());
        existingInstructor.setInstructorAddress(instructor.getInstructorAddress());
        existingInstructor.setInstructorImg(instructor.getInstructorImg());
        existingInstructor.setDateOfBirth(instructor.getDateOfBirth());
        existingInstructor.setInstructorGender(instructor.getInstructorGender());

        return instructorRepository.save(existingInstructor);
    }

    /**
     * Delete an instructor by ID
     *
     * @param id Instructor ID
     * @throws ResourceNotFoundException if instructor not found
     */
    public void deleteInstructor(int id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with ID: " + id));
        instructorRepository.delete(instructor);
    }

    @Override
    public void save(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    @Override
    public Instructor instructorLimit() {
        return instructorRepository.limitInstructor();
    }
}


