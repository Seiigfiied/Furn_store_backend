package com.chesnakov.hardwareecommerce.controller;

import com.chesnakov.hardwareecommerce.entity.OrderDetail;
import com.chesnakov.hardwareecommerce.entity.OrderInput;
import com.chesnakov.hardwareecommerce.service.OrderDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }
    @PostMapping({"/placeOrder/{isSingleProductCheckout}"})
    @PreAuthorize("hasRole('User')")
    public void placeOrder(@RequestBody OrderInput orderInput,
                           @PathVariable("isSingleProductCheckout") boolean isSingleProductCheckout) {
        orderDetailService.placeOrder(orderInput, isSingleProductCheckout);
    }
    @GetMapping({"/getOrderDetails"})
    @PreAuthorize("hasRole('User')")
    public List<OrderDetail> getOrderDetails(){
        return orderDetailService.getOrderDetails();
    }
    @GetMapping({"/getAllOrders/{status}"})
    @PreAuthorize("hasRole('Admin')")
    public List<OrderDetail> getAllOrders(@PathVariable("status") String status) {
        return orderDetailService.getAllOrders(status);
    }
    @PreAuthorize("hasRole('Admin')")
    @GetMapping({"/markOrderAsDelivered/{orderId}"})
    public void markOrderAsDelivered(@PathVariable("orderId") Long orderId) {
        orderDetailService.markOrderAsDelivered(orderId);
    }
}
