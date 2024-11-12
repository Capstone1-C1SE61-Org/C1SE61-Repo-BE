package com.example.systemp3l.repository;


import com.example.systemp3l.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IInstructorRepository extends JpaRepository<Instructor, Integer> {

    @Query(value = "select * from instructor order by `instructor_code` desc limit 1", nativeQuery = true)
    Instructor limitInstructor();
}
