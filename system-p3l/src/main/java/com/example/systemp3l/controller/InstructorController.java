package com.example.systemp3l.controller;

import com.example.systemp3l.dto.Instructor.InstructorInfo;
import com.example.systemp3l.dto.Instructor.InstructorUserDetailDto;
import com.example.systemp3l.dto.response.MessageResponse;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.service.IInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/instructor")
public class InstructorController {

    @Autowired
    private IInstructorService instructorService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Instructor>> findAllInstructor(@RequestParam(value = "name", required = false) Optional<String> name,
                                                            @RequestParam(value = "address", required = false) Optional<String> address,
                                                            @RequestParam(value = "phone", required = false) Optional<String> phone,
                                                            @RequestParam("page") Optional<Integer> page,
                                                            @RequestParam("size") Optional<Integer> size,
                                                            @RequestParam("sort") Optional<String> sort) {
        String searchName = name.orElse("");
        String searchAddress = address.orElse("");
        String searchPhone = phone.orElse("");
        int pages = page.orElse(1);
        int pageSize = size.orElse(5);
        String sortName = sort.orElse("instructor_name");
        Page<Instructor> searchInstructors = instructorService.searchInstructors(searchName, searchAddress, searchPhone,
                PageRequest.of(pages - 1, pageSize, Sort.by(sortName)));
        if (searchInstructors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(searchInstructors, HttpStatus.OK);
        }
    }

    @GetMapping("/{instructorId}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Integer instructorId) {
        return new ResponseEntity<>(instructorService.findById(instructorId), HttpStatus.OK);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateInstructor(@Valid @PathVariable Integer id, @RequestBody InstructorInfo instructorInfo,
                                            BindingResult bindingResult) {
        new InstructorInfo().validate(instructorInfo, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        } else {
            instructorService.updateInstructor(instructorInfo, id);
        }

        return new ResponseEntity<>(new MessageResponse("The instructor has successfully edited!"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        try {
            instructorService.deleteById(id);
            return new ResponseEntity<>(new MessageResponse("Instructor deleted successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Failed to delete instructor: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

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
