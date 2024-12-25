package com.example.systemp3l.repository;


import com.example.systemp3l.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.Optional;

@Repository
@Transactional
public interface IInstructorRepository extends JpaRepository<Instructor, Integer> {

    @Query(value = "select * from instructor order by `instructor_code` desc limit 1", nativeQuery = true)
    Instructor limitInstructor();

    @Query(value = "select i.instructor_id, i.instructor_code, i.instructor_name, i.instructor_phone, i.instructor_gender, " +
            "i.date_of_birth, i.instructor_address, i.instructor_img, a.username, a.email " +
            "from instructor i " +
            "inner join account a on i.account_id = a.account_id " +
            "where (i.is_enable = true) and (a.is_enable = true) and (a.username = :username)", nativeQuery = true)
    Optional<Tuple> findUserDetailByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM instructor i JOIN account a ON i.account_id = a.account_id WHERE a.username = ?1",
            nativeQuery = true)
    Instructor findByUsername(String username);
}
