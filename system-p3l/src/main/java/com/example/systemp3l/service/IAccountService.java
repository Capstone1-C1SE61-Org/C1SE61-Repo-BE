package com.example.systemp3l.service;

import com.example.systemp3l.model.Account;

public interface IAccountService {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Account save(Account account);

    void changePassword(String username, String newPass);
}
