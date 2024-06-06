package com.chesnakov.hardwareecommerce.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    private Long cartId;

    @OneToOne
    private Product product;

    @OneToOne
    private User user;

    public Cart(Product product, User user) {
        this.product = product;
        this.user = user;
    }
}
