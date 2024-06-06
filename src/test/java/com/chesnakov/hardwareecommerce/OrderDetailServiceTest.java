package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.configuration.JwtRequestFilter;
import com.chesnakov.hardwareecommerce.dao.CartDao;
import com.chesnakov.hardwareecommerce.dao.OrderDetailDao;
import com.chesnakov.hardwareecommerce.dao.ProductDao;
import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.*;
import com.chesnakov.hardwareecommerce.service.OrderDetailService;
import com.chesnakov.hardwareecommerce.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderDetailServiceTest {

    @Mock
    private OrderDetailDao orderDetailDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private CartDao cartDao;

    @InjectMocks
    private OrderDetailService orderDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        JwtRequestFilter.CURRENT_USER = "jsmith";
    }

    @Test
    public void testPlaceOrder() {
        // Given
        OrderInput orderInput = new OrderInput();
        orderInput.setFullName("John Smith");
        orderInput.setFullAddress("123 Main St.");
        orderInput.setContactNumber("555-1234");
        orderInput.setAlternateContactNumber("555-5678");

        User user = new User();
        user.setUserName("jsmith");
        user.setUserPassword("password");

        when(userDao.findById(JwtRequestFilter.CURRENT_USER)).thenReturn(Optional.of(user));

        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Product 1");
        product.setProductDiscountedPrice(10.0);

        when(productDao.findById(1L)).thenReturn(Optional.of(product));

        List<OrderProductQuantity> productQuantityList = new ArrayList<>();
        OrderProductQuantity productQuantity = new OrderProductQuantity();
        productQuantity.setProductId(1L);
        productQuantity.setQuantity(2L);
        productQuantityList.add(productQuantity);

        orderInput.setOrderProductQuantityList(productQuantityList);

        // When
        orderDetailService.placeOrder(orderInput, false);

        // Then
        verify(orderDetailDao, times(1)).save(any(OrderDetail.class));
        verify(cartDao).deleteAllById(anyList());
    }

    @Test
    public void testGetOrderDetails() {
        // Given
        User user = new User();
        user.setUserName("jsmith");
        user.setUserPassword("password");

        when(userDao.findById(JwtRequestFilter.CURRENT_USER)).thenReturn(Optional.of(user));

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setOrderId(1L);
        orderDetail1.setOrderStatus("Placed");
        orderDetail1.setUser(user);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setOrderId(2L);
        orderDetail2.setOrderStatus("Delivered");
        orderDetail2.setUser(user);

        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail1);
        orderDetails.add(orderDetail2);

        when(orderDetailDao.findByUser(user)).thenReturn(orderDetails);

        // When
        List<OrderDetail> result = orderDetailService.getOrderDetails();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderId().longValue());
        assertEquals(2L, result.get(1).getOrderId().longValue());
    }

    @Test
    public void testGetAllOrders() {
        Set<Role> role1 = new HashSet<>();
        role1.add(new Role("1234567890"));

        Set<Role> role2 = new HashSet<>();
        role2.add(new Role("0987654321"));

        List<OrderDetail> orderDetailList = new ArrayList<>();
        User user1 = new User("user1", "password", "User1", "user1@gmail.com", role1);
        User user2 = new User("user2", "password", "User2", "user2@gmail.com", role2);
        OrderDetail orderDetail1 = new OrderDetail("User1", "Address1", "1234567890", "1111111111", "Delivered", 1000.0, new Product(), user1);
        OrderDetail orderDetail2 = new OrderDetail("User2", "Address2", "0987654321", "2222222222", "Placed", 2000.0, new Product(), user2);
        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);
        when(orderDetailDao.findAllByOrderByUserUserName()).thenReturn(orderDetailList);

        List<OrderDetail> result = orderDetailService.getAllOrders("All");

        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getOrderFullName());
        assertEquals("User2", result.get(1).getOrderFullName());
    }

    @Test
    public void testMarkOrderAsDelivered() {
        OrderDetail orderDetail = new OrderDetail("User1", "Address1", "1234567890", "1111111111", "Placed", 1000.0, new Product(), new User());
        when(orderDetailDao.findById(1L)).thenReturn(Optional.of(orderDetail));

        orderDetailService.markOrderAsDelivered(1L);

        assertEquals("Delivered", orderDetail.getOrderStatus());
        verify(orderDetailDao, times(1)).save(orderDetail);
    }
}