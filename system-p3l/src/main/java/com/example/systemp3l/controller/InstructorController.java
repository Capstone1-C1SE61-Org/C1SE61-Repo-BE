package com.example.systemp3l.controller;

import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.service.impl.InstructorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorController {

    @Autowired
    private InstructorServiceImpl instructorService;

    /**
     * Get all instructors
     *
     * @return List of instructors
     */
    @GetMapping
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        List<Instructor> instructors = instructorService.getAllInstructors();
        return ResponseEntity.ok(instructors);
    }

    /**
     * Get an instructor by ID
     *
     * @param id Instructor ID
     * @return Instructor object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable int id) {
        Instructor instructor = instructorService.getInstructorById(id);
        return ResponseEntity.ok(instructor);
    }

    /**
     * Create a new instructor
     *
     * @param instructor Instructor object
     * @return Created instructor
     */
    @PostMapping("/new")
    public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor) {
        Instructor createdInstructor = instructorService.createInstructor(instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInstructor);
    }

    /**
     * Update an existing instructor
     *
     * @param id         Instructor ID
     * @param instructor Updated instructor object
     * @return Updated instructor
     */
    @PutMapping("/{id}")
    public ResponseEntity<Instructor> updateInstructor(@PathVariable int id, @RequestBody Instructor instructor) {
        Instructor updatedInstructor = instructorService.updateInstructor(id, instructor);
        return ResponseEntity.ok(updatedInstructor);
    }

    /**
     * Delete an instructor by ID
     *
     * @param id Instructor ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInstructor(@PathVariable int id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.ok("Instructor with ID " + id + " deleted successfully.");
    }
}
