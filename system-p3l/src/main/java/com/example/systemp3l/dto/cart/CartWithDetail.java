package com.example.systemp3l.dto.cart;

import com.example.systemp3l.model.Cart;
import com.example.systemp3l.model.CartDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartWithDetail {
    private Cart cart;
    private List<CartDetail> cartDetailList;
}
