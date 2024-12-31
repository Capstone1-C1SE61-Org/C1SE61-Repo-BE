package com.example.systemp3l.controller;

import com.example.systemp3l.model.Account;
import com.example.systemp3l.model.Role;
import com.example.systemp3l.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private IRoleService roleService;

    @PostMapping("/addAccount")
    public ResponseEntity<?> addAccount(@Valid @RequestBody Account account, BindingResult bindingResult,
                                        @RequestParam Integer roleId) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> {
                        String fieldName = error.getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                    });
            return ResponseEntity.badRequest().body(errors);
        }
        Role role = roleService.findById(roleId);
        if (role == null) {
            return ResponseEntity.badRequest().body("Invalid roleId");
        }
        Set<Role> tempRoles = account.getRoles();
        tempRoles.add(role);
        account.setRoles(tempRoles);
        return ResponseEntity.ok(account);

    }
}
