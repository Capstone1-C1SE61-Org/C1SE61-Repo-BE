package com.example.systemp3l.controller;

import com.example.systemp3l.dto.Instructor.InstructorUserDetailDto;
import com.example.systemp3l.service.IInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/instructor")
public class InstructorController {

    @Autowired
    private IInstructorService instructorService;

    @GetMapping("/detail")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<InstructorUserDetailDto> getDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        InstructorUserDetailDto instructorUserDetailDto = instructorService.findUserDetailByUsername(username);

        if (instructorUserDetailDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(instructorUserDetailDto, HttpStatus.OK);
    }
}
