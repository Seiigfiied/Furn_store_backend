package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.configuration.JwtRequestFilter;
import com.chesnakov.hardwareecommerce.dao.CartDao;
import com.chesnakov.hardwareecommerce.dao.ProductDao;
import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.Cart;
import com.chesnakov.hardwareecommerce.entity.Product;
import com.chesnakov.hardwareecommerce.entity.User;
import com.chesnakov.hardwareecommerce.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartDao cartDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private UserDao userDao;

    private CartService cartService;

    private Product product;
    private User user;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cartService = new CartService(cartDao, productDao, userDao);

        product = new Product();
        product.setProductId(1L);
        product.setProductName("Product 1");
        product.setProductActualPrice(100.0);
        product.setProductDiscountedPrice(100.0);

        user = new User();
        user.setUserName("user1");
        JwtRequestFilter.CURRENT_USER = "user1";

        cart = new Cart(product, user);
        cart.setCartId(1L);
    }

    @Test
    public void testAddToCartSuccess() {
        when(productDao.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(userDao.findById(user.getUserName())).thenReturn(Optional.of(user));
        when(cartDao.existsByProductAndUser(product, user)).thenReturn(false);
        when(cartDao.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addToCart(product.getProductId());

        assertEquals(cart, result);
        verify(productDao, times(1)).findById(product.getProductId());
        verify(userDao, times(1)).findById(user.getUserName());
        verify(cartDao, times(1)).existsByProductAndUser(product, user);
        verify(cartDao, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddToCartProductAlreadyInCart() {
        when(productDao.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(userDao.findById(user.getUserName())).thenReturn(Optional.of(user));
        when(cartDao.existsByProductAndUser(product, user)).thenReturn(true);

        Cart result = cartService.addToCart(product.getProductId());

        assertNull(result);
        verify(productDao, times(1)).findById(product.getProductId());
        verify(userDao, times(1)).findById(user.getUserName());
        verify(cartDao, times(1)).existsByProductAndUser(product, user);
        verify(cartDao, never()).save(any(Cart.class));
    }

    @Test
    public void testGetCartDetails() {
        when(userDao.findById(user.getUserName())).thenReturn(Optional.of(user));
    }
}