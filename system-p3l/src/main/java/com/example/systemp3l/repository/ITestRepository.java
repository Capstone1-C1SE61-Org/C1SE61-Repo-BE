package com.example.systemp3l.repository;

import com.example.systemp3l.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ITestRepository extends JpaRepository<Test, Integer> {

}
