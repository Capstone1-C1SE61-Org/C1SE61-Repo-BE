package com.example.systemp3l.controller;

import com.example.systemp3l.dto.request.LoginRequest;
import com.example.systemp3l.dto.request.SignupRequest;
import com.example.systemp3l.dto.response.JwtResponse;
import com.example.systemp3l.dto.response.MessageResponse;
import com.example.systemp3l.model.Account;
import com.example.systemp3l.model.Customer;
import com.example.systemp3l.model.Instructor;
import com.example.systemp3l.model.Role;
import com.example.systemp3l.security.jwt.JwtUtility;
import com.example.systemp3l.security.userprinciple.UserPrinciple;
import com.example.systemp3l.service.IAccountService;
import com.example.systemp3l.service.ICustomerService;
import com.example.systemp3l.service.IInstructorService;
import com.example.systemp3l.utils.ConverterMaxCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public")
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IInstructorService instructorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtility.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        roles
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
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

        Set<Role> tempRoles = account.getRoles();
        String userType = signupRequest.getUserType();

        if ("instructor".equalsIgnoreCase(userType)) {
            Role instructorRole = new Role(3, "ROLE_INSTRUCTOR");
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
        } else {
            Role customerRole = new Role(2, "ROLE_CUSTOMER");
            tempRoles.add(customerRole);
            account.setRoles(tempRoles);

            Customer customerLimit = customerService.customerLimit();
            signupRequest.setCode(ConverterMaxCode.generateNextId(customerLimit.getCustomerCode()));

            customerService.save(new Customer(
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
        }

        return new ResponseEntity<>(new MessageResponse("Account registration successful!"), HttpStatus.OK);
    }
}
