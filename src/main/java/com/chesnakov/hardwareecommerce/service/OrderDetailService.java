package com.chesnakov.hardwareecommerce.service;

import com.chesnakov.hardwareecommerce.configuration.JwtRequestFilter;
import com.chesnakov.hardwareecommerce.dao.CartDao;
import com.chesnakov.hardwareecommerce.dao.OrderDetailDao;
import com.chesnakov.hardwareecommerce.dao.ProductDao;
import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.*;
import com.chesnakov.hardwareecommerce.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {

    private static final String ORDER_PLACED = "Placed";

    private final OrderDetailDao orderDetailDao;
    private final UserDao userDao;

    private final ProductDao productDao;
    private final CartDao cartDao;

    public OrderDetailService(OrderDetailDao orderDetailDao, UserDao userDao, ProductDao productDao, CartDao cartDao) {
        this.orderDetailDao = orderDetailDao;
        this.userDao = userDao;
        this.productDao = productDao;
        this.cartDao = cartDao;
    }

    public void placeOrder(OrderInput orderInput, boolean isSingleProductCheckout) {
        List<OrderProductQuantity> productQuantityList = orderInput.getOrderProductQuantityList();
        User user = userDao.findById(JwtRequestFilter.CURRENT_USER).get();

        for (OrderProductQuantity productQuantity : productQuantityList) {
            Product product = productDao.findById(productQuantity.getProductId()).get();

            OrderDetail orderDetail = new OrderDetail(
                    orderInput.getFullName(),
                    orderInput.getFullAddress(),
                    orderInput.getContactNumber(),
                    orderInput.getAlternateContactNumber(),
                    ORDER_PLACED,
                    product.getProductDiscountedPrice() * productQuantity.getQuantity(),
                    product,
                    user
            );

            orderDetailDao.save(orderDetail);
        }

        if (!isSingleProductCheckout) {
            List<Cart> carts = cartDao.findByUser(user);
            cartDao.deleteAllById(carts.stream().map(Cart::getCartId).collect(Collectors.toList()));
        }
    }

    public List<OrderDetail> getOrderDetails() {
        User user = userDao.findById(JwtRequestFilter.CURRENT_USER).get();

        return orderDetailDao.findByUser(user);
    }

    public List<OrderDetail> getAllOrders(String status) {
        if (status.equals("All")) {
            return orderDetailDao.findAllByOrderByUserUserName();
        }
        else {
            return orderDetailDao.findByOrderStatus(status);
        }
    }

    public void markOrderAsDelivered(Long orderId) {
        OrderDetail orderDetail = orderDetailDao.findById(orderId).get();

        if (orderDetail != null) {
            orderDetail.setOrderStatus("Delivered");
            orderDetailDao.save(orderDetail);
        }
    }
 }
