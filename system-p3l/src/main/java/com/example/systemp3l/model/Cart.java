package com.example.systemp3l.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;
    @NotBlank
    @Pattern(regexp = "^(?:[A-Z][a-zÀ-ỹ]*(?: [A-Z][a-zÀ-ỹ]*)+)$")
    private String receiverName;
    @NotBlank
    private String receiverAddress;
    @NotBlank
    @Email
    private String receiverEmail;
    @NotBlank
    @Pattern(regexp = "^0\\d{9}$")
    private String receiverPhone;

    @JsonBackReference
    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    private Customer customer;
}
