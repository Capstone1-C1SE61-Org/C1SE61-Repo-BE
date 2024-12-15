package com.example.systemp3l.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Tuple;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserDetailDto {
    Integer customerId;
    String customerCode;
    String customerName;
    String customerPhone;
    Boolean customerGender;
    Date dateOfBirth;
    String customerAddress;
    String customerImg;
    String customerTypeName;
    String username;
    String accountEmail;

    public static CustomerUserDetailDto TupleToCustomerDto(Tuple tuple) {
        return new CustomerUserDetailDto(
                tuple.get("customer_id", Integer.class),
                tuple.get("customer_code", String.class),
                tuple.get("customer_name", String.class),
                tuple.get("customer_phone", String.class),
                tuple.get("customer_gender", Boolean.class),
                tuple.get("date_of_birth", Date.class),
                tuple.get("customer_address", String.class),
                tuple.get("customer_img", String.class),
                tuple.get("customer_type_name", String.class),
                tuple.get("username", String.class),
                tuple.get("email", String.class)
        );
    }
}
