package com.example.systemp3l.repository;

import com.example.systemp3l.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.Optional;

@Repository
@Transactional
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "select * from customer order by `customer_code` desc limit 1", nativeQuery = true)
    Customer limitCustomer();

    @Query(value = "select c.customer_id, c.customer_code, c.customer_name, c.customer_phone, c.customer_gender, " +
            "c.date_of_birth, c.customer_address, c.customer_img, a.username, a.email " +
            "from customer c " +
            "inner join account a on c.account_id = a.account_id " +
            "where (c.is_enable = true) and (a.is_enable = true) and (a.username = :username)", nativeQuery = true)
    Optional<Tuple> findUserDetailByUsername(@Param("username") String username);

    @Query(value = "SELECT c.* FROM customer c JOIN account a ON c.account_id = a.account_id WHERE a.username = ?1",
            nativeQuery = true)
    Customer findCustomerByUsername(String username);
}
