package com.example.systemp3l.repository;

import com.example.systemp3l.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface IAccountRepository extends JpaRepository<Account, Integer> {

    @Query(value = "select account_id, username, encrypt_password, email, is_enable " +
            "from account " +
            "where username = :username and is_enable = true", nativeQuery = true)
    Optional<Account> findAccountByUsername(@Param("username") String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Query(value = "update `account` a set a.encrypt_password =:pass " +
            "where (a.is_enable = true) and (username = :username) ",
            nativeQuery = true)
    void changePassword(@Param("username") String username,
                        @Param("pass") String pass);
}
