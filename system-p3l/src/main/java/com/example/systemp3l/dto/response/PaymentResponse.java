package com.example.systemp3l.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentResponse implements Serializable {
    private String status;
    private String message;
    private String url;
}
