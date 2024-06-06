package com.chesnakov.hardwareecommerce.controller;

import com.chesnakov.hardwareecommerce.dao.CartDao;
import com.chesnakov.hardwareecommerce.entity.Cart;
import com.chesnakov.hardwareecommerce.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {
    private final CartService cartService;
    private final CartDao cartDao;
    public CartController(CartService cartService,
                          CartDao cartDao) {
        this.cartService = cartService;
        this.cartDao = cartDao;
    }
    @PreAuthorize("hasRole('User')")
    @GetMapping({"/addToCart/{productId}"})
    public Cart addToCart(@PathVariable("productId") Long productId) {
        return cartService.addToCart(productId);
    }
    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getCartDetails"})
    public List<Cart> getCartDetails() {
        return cartService.getCartDetails();
    }

    @PreAuthorize("hasRole('User')")
    @DeleteMapping({"/deleteCartItem/{cartId}"})
    public void deleteCartItem(@PathVariable("cartId") Long cartId) {
        cartService.deleteCartItem(cartId);
    }
}
