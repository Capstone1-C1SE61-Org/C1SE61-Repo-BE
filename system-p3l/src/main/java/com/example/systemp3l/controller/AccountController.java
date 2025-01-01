package com.example.systemp3l.controller;

import com.example.systemp3l.dto.request.SignupRequest;
import com.example.systemp3l.dto.response.MessageResponse;
import com.example.systemp3l.model.Account;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.model.Role;
import com.example.systemp3l.service.IAccountService;
import com.example.systemp3l.service.IInstructorService;
import com.example.systemp3l.utils.ConverterMaxCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IInstructorService instructorService;

    @PostMapping("/add-instructor")
    public ResponseEntity<?> addInstructorAccount(@RequestBody SignupRequest signupRequest) {
        if (accountService.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity<>(new MessageResponse("The username existed! Please try again!"), HttpStatus.OK);
        }

        if (accountService.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>(new MessageResponse("The email existed! Please try again!"), HttpStatus.OK);
        }

        Account account = new Account();
        account.setUsername(signupRequest.getUsername());
        account.setEncryptPassword(passwordEncoder.encode(signupRequest.getPassword()));
        account.setEmail(signupRequest.getEmail());
        account.setIsEnable(true);
        accountService.save(account);

        Role instructorRole = new Role(3, "ROLE_INSTRUCTOR");
        Set<Role> tempRoles = account.getRoles();
        tempRoles.add(instructorRole);
        account.setRoles(tempRoles);

        Instructor instructorLimit = instructorService.instructorLimit();
        signupRequest.setCode(ConverterMaxCode.generateNextId(instructorLimit.getInstructorCode()));

        instructorService.save(new Instructor(
                signupRequest.getCode(),
                signupRequest.getName(),
                signupRequest.getEmail(),
                signupRequest.getPhone(),
                signupRequest.getGender(),
                signupRequest.getDateOfBirth(),
                signupRequest.getAddress(),
                true,
                account
        ));

        return new ResponseEntity<>(new MessageResponse("Instructor account registration successful!"), HttpStatus.OK);
    }


}
