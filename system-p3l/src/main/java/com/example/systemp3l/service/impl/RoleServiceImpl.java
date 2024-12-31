package com.example.systemp3l.service.impl;

import com.example.systemp3l.model.Role;
import com.example.systemp3l.repository.IRoleRepository;
import com.example.systemp3l.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }
}
