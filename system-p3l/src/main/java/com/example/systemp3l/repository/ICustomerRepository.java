package com.example.systemp3l.repository;

import com.example.systemp3l.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "select * from customer order by `customer_code` desc limit 1", nativeQuery = true)
    Customer limitCustomer();
}
