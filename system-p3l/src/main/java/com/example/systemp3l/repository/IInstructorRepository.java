package com.example.systemp3l.repository;

import com.example.systemp3l.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.sql.Date;
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

    @Query(value = "select i.instructor_id, i.date_of_birth, i.instructor_address, i.instructor_code, i.instructor_email, " +
            "i.instructor_gender, i.instructor_img, i.instructor_name, i.instructor_phone, i.is_enable, a.account_id " +
            "from instructor i " +
            "left join account a on i.account_id = a.account_id " +
            "where (i.instructor_name like concat('%',:name,'%') and i.instructor_address like concat('%',:address,'%') " +
            "and i.instructor_phone like concat('%',:phone,'%')) and (i.is_enable = true) " +
            "order by i.instructor_code ",
            countQuery = "select count(i.instructor_id) " +
                    "from instructor i " +
                    "left join account a on i.account_id = a.account_id " +
                    "where (i.instructor_name like concat('%',:name,'%') and i.instructor_address like concat('%',:address,'%') " +
                    "and i.instructor_phone like concat('%',:phone,'%')) and (i.is_enable = true) " +
                    "order by i.instructor_code", nativeQuery = true)
    Page<Instructor> searchInstructor(@Param("name") String name, @Param("address") String address,
                                      @Param("phone") String phone, Pageable pageable);


    @Modifying
    @Query(value = "UPDATE instructor SET `account_id`=:account_id, `date_of_birth`=:date_of_birth, " +
            "`instructor_address`=:instructor_address, `instructor_code`=:instructor_code, " +
            "`instructor_email`=:instructor_email, `instructor_gender`=:instructor_gender, " +
            "`instructor_img`=:instructor_img, `instructor_name`=:instructor_name, `instructor_phone`=:instructor_phone, " +
            "`is_enable`=:is_enable WHERE `instructor_id`=:instructor_id", nativeQuery = true)
    void updateInstructor(@Param("instructor_id") Integer instructor_id,
                          @Param("instructor_code") String instructor_code,
                          @Param("instructor_name") String instructor_name,
                          @Param("instructor_email") String instructor_email,
                          @Param("instructor_phone") String instructor_phone,
                          @Param("instructor_gender") Boolean instructor_gender,
                          @Param("date_of_birth") Date date_of_birth,
                          @Param("instructor_address") String instructor_address,
                          @Param("instructor_img") String instructor_img,
                          @Param("is_enable") Boolean is_enable,
                          @Param("account_id") Integer account_id);

    @Modifying
    @Query(value = "update instructor set is_enable = false where instructor_id = :id", nativeQuery = true)
    void deleteInstructorId(@Param("id") Integer id);
}
