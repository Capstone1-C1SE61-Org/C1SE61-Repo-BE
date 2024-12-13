package com.example.systemp3l.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Customer_test_result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer question_ID;
    private boolean is_passed;
    private long score;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "test_id")
    private Test test;

}
