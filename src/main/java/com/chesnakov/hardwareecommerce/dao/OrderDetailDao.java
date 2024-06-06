package com.chesnakov.hardwareecommerce.dao;

import com.chesnakov.hardwareecommerce.entity.OrderDetail;
import com.chesnakov.hardwareecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderStatus(String orderStatus);
    List<OrderDetail> findByUser(User user);

    List<OrderDetail> findAllByOrderByUserUserName();
}
