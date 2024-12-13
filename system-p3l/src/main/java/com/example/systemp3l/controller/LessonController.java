package com.example.systemp3l.controller;

import com.example.systemp3l.model.Lesson;
import com.example.systemp3l.service.impl.LessonServiceImpl;
import com.example.systemp3l.service.impl.LessonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonController {

    @Autowired
    private LessonServiceImpl lessonService;

    /**
     * Get all lessons by course id
     *
     * @return List of all lessons
     */
    @GetMapping("/course/{id}")
    public ResponseEntity<List<Lesson>> getAllByCourseID(@PathVariable Integer id) {
        List<Lesson> lessons = lessonService.getAllByCourseID(id);
        return ResponseEntity.ok(lessons);
    }


    /**
     * Get a specific lesson by ID
     *
     * @param id Lesson ID
     * @return Lesson object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable int id) {
        Lesson lesson = lessonService.getLessonById(id);
        return ResponseEntity.ok(lesson);
    }

    /**
     * Create a new lesson
     *
     * @param lesson Lesson object
     * @return Created lesson
     */
    @PostMapping
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        Lesson createdLesson = lessonService.createLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    /**
     * Update an existing lesson
     *
     * @param id      Lesson ID
     * @param lesson  Updated lesson object
     * @return Updated lesson
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable int id, @RequestBody Lesson lesson) {
        Lesson updatedLesson = lessonService.updateLesson(id, lesson);
        return ResponseEntity.ok(updatedLesson);
    }

    /**
     * Delete a lesson by ID
     *
     * @param id Lesson ID
     * @return Response message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Integer id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok("Lesson deleted successfully.");
    }
}
