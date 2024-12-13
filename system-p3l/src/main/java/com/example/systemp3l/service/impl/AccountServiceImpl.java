package com.example.systemp3l.service.impl;

import com.example.systemp3l.Exception.ResourceNotFoundException;
import com.example.systemp3l.model.Account;
import com.example.systemp3l.repository.IAccountRepository;
import com.example.systemp3l.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Integer id, Account accountDetails) {
        Account account = getAccountById(id);
        account.setUsername(accountDetails.getUsername());
        account.setEmail(accountDetails.getEmail());
        account.setEncryptPassword(accountDetails.getEncryptPassword());
        //account.setRole(accountDetails.getRole());
        return accountRepository.save(account);
    }

    public void deleteAccount(Integer id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }
    @Override
    public Boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
