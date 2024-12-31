package com.example.systemp3l.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;
    private String customerCode;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Boolean customerGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String customerAddress;
    private String customerImg;
    private Boolean isEnable;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @JsonBackReference
    @ManyToMany(mappedBy = "customers")
    private Set<Course> courses = new LinkedHashSet<>();

    public Customer(String customerCode, String customerName, String customerEmail, String customerPhone,
                    Boolean customerGender, Date dateOfBirth, String customerAddress, Boolean isEnable,
                    Account account) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerGender = customerGender;
        this.dateOfBirth = dateOfBirth;
        this.customerAddress = customerAddress;
        this.isEnable = isEnable;
        this.account = account;
    }
}
