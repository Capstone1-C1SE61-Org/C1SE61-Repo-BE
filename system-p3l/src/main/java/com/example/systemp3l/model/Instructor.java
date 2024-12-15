package com.example.systemp3l.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer instructorId;
    private String instructorCode;
    private String instructorName;
    private String instructorEmail;
    private String instructorPhone;
    private Boolean instructorGender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String instructorAddress;
    private String instructorImg;
    private Boolean isEnable;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public Instructor(String instructorCode, String instructorName, String instructorEmail, String instructorPhone,
                      Boolean instructorGender, Date dateOfBirth, String instructorAddress, Boolean isEnable,
                      Account account) {
        this.instructorCode = instructorCode;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
        this.instructorGender = instructorGender;
        this.dateOfBirth = dateOfBirth;
        this.instructorAddress = instructorAddress;
        this.isEnable = isEnable;
        this.account = account;
    }
}
