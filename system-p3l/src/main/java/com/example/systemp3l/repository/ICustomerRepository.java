package com.example.systemp3l.repository;

import com.example.systemp3l.model.Customer;
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

    @Query(value = "select c.customer_id, c.customer_address, c.customer_code, c.customer_email, c.customer_gender, " +
            "c.customer_img, c.customer_name, c.customer_phone, c.date_of_birth, c.is_enable, a.account_id, r.cart_id " +
            "from customer c " +
            "left join account a on c.account_id = a.account_id " +
            "left join cart r on c.cart_id = r.cart_id "+
            "where (c.customer_name like concat('%',:name,'%') and c.customer_address like concat('%',:address,'%') " +
            "and c.customer_phone like concat('%',:phone,'%')) and (c.is_enable = true) " +
            "order by c.customer_code ",
            countQuery = "select count(c.customer_id) " +
                    "from customer c " +
                    "left join account a on c.account_id = a.account_id  " +
                    "left join cart r on c.cart_id = r.cart_id " +
                    "where (c.customer_name like concat('%',:name,'%') and c.customer_address like concat('%',:address,'%') " +
                    "and c.customer_phone like concat('%',:phone,'%')) and (c.is_enable = true) " +
                    "order by c.customer_code", nativeQuery = true)
    Page<Customer> searchCustomer(@Param("name") String name, @Param("address") String address,
                                  @Param("phone") String phone, Pageable pageable);

    @Modifying
    @Query(value = "INSERT INTO customer (customer_name, customer_email, customer_address, customer_code, " +
            "customer_gender, customer_img, customer_phone, date_of_birth, is_enable, cart_id) " +
            "VALUES (:customer_name, :customer_email, :customer_address, :customer_code, :customer_gender, :customer_img," +
            ":customer_phone, :date_of_birth, :is_enable, :cart_id)", nativeQuery = true)
    void insertCustomer(@Param("customer_name") String customer_name,
                        @Param("customer_email") String customer_email,
                        @Param("customer_address") String customer_address,
                        @Param("customer_code") String customer_code,
                        @Param("customer_gender") Boolean customer_gender,
                        @Param("customer_img") String customer_img,
                        @Param("customer_phone") String customer_phone,
                        @Param("date_of_birth") Date date_of_birth,
                        @Param("is_enable") Boolean is_enable,
                        @Param("cart_id") Integer cart_id);

    @Modifying
    @Query(value = "UPDATE customer SET `account_id`=:account_id, `cart_id`=:cart_id, `customer_address`=:customer_address, " +
            "`customer_code`=:customer_code,`customer_email`=:customer_email, `customer_gender`=:customer_gender, " +
            "`customer_img`=:customer_img, `customer_name`=:customer_name, `customer_phone`=:customer_phone, " +
            "`date_of_birth`=:date_of_birth, `is_enable`=:is_enable WHERE `customer_id`=:customer_id", nativeQuery = true)
    void updateCustomer(@Param("customer_id") Integer customer_id,
                        @Param("customer_code") String customer_code,
                        @Param("customer_name") String customer_name,
                        @Param("customer_email") String customer_email,
                        @Param("customer_phone") String customer_phone,
                        @Param("customer_gender") Boolean customer_gender,
                        @Param("date_of_birth") Date date_of_birth,
                        @Param("customer_address") String customer_address,
                        @Param("customer_img") String customer_img,
                        @Param("is_enable") Boolean is_enable,
                        @Param("account_id") Integer account_id,
                        @Param("cart_id") Integer cart_id);

    @Modifying
    @Query(value = "update customer set is_enable = false where customer_id = :id", nativeQuery = true)
    void deleteCustomerId(@Param("id") Integer id);
}
