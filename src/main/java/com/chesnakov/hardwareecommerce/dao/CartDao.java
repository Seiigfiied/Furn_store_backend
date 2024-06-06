package com.chesnakov.hardwareecommerce.dao;

import com.chesnakov.hardwareecommerce.entity.Cart;
import com.chesnakov.hardwareecommerce.entity.Product;
import com.chesnakov.hardwareecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends JpaRepository<Cart, Long> {
    boolean existsByProductAndUser(Product product, User user);

    List<Cart> findByUser(User user);
}
