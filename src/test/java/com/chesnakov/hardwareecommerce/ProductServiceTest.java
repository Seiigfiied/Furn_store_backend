package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.configuration.JwtRequestFilter;
import com.chesnakov.hardwareecommerce.dao.CartDao;
import com.chesnakov.hardwareecommerce.dao.ProductDao;
import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.Cart;
import com.chesnakov.hardwareecommerce.entity.ImageModel;
import com.chesnakov.hardwareecommerce.entity.Product;
import com.chesnakov.hardwareecommerce.entity.User;
import com.chesnakov.hardwareecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private UserDao userDao;

    @Mock
    private CartDao cartDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        JwtRequestFilter.CURRENT_USER = "jsmith";
    }

    @Test
    public void addNewProduct() {
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setProductDescription("Test Product Description");
        product.setProductActualPrice(100.0);
        product.setProductDiscountedPrice(80.0);

        Set<ImageModel> imageModels = new HashSet<>();
        imageModels.add(new ImageModel("test", "jpg", new byte[]{1, 2, 3}));
        product.setProductImages(imageModels);

        when(productDao.save(product)).thenReturn(product);

        Product savedProduct = productService.addNewProduct(product);

        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductDescription(), savedProduct.getProductDescription());
        assertEquals(product.getProductActualPrice(), savedProduct.getProductActualPrice(), 0.0);
        assertEquals(product.getProductDiscountedPrice(), savedProduct.getProductDiscountedPrice(), 0.0);
        assertEquals(product.getProductImages(), savedProduct.getProductImages());
    }

    @Test
    public void getAllProducts() {
        List<Product> productList = new ArrayList<>();
        Set<ImageModel> imageModels = new HashSet<>();
        imageModels.add(new ImageModel("test", "jpg", new byte[]{1, 2, 3}));
        productList.add(new Product(1L, "Test Product 1", "Test Product Description 1", 100.0, 80.0, imageModels));
        productList.add(new Product(2L, "Test Product 2", "Test Product Description 2", 200.0, 160.0, imageModels));

        when(productDao.findAll()).thenReturn(productList);

        List<Product> retrievedProducts = productService.getAllProducts();

        assertEquals(productList.size(), retrievedProducts.size());
        assertEquals(productList.get(0).getProductName(), retrievedProducts.get(0).getProductName());
        assertEquals(productList.get(1).getProductDescription(), retrievedProducts.get(1).getProductDescription());
    }

    @Test
    public void deleteProductDetails() {
        Long productId = 1L;

        productService.deleteProductDetails(productId);

        verify(productDao).deleteById(productId);
    }

    @Test
    public void getProductDetailsById() {
        Long productId = 1L;
        Set<ImageModel> imageModels = new HashSet<>();
        imageModels.add(new ImageModel("test", "jpg", new byte[]{1, 2, 3}));
        Product product = new Product(1L, "Test Product", "Test Product Description", 100.0, 80.0, imageModels);

        when(productDao.findById(productId)).thenReturn(Optional.of(product));

        Product retrievedProduct = productService.getProductDetailsById(productId);

        assertEquals(product.getProductName(), retrievedProduct.getProductName());
        assertEquals(product.getProductDescription(), retrievedProduct.getProductDescription());
        assertEquals(product.getProductActualPrice(), retrievedProduct.getProductActualPrice(), 0.0);
        assertEquals(product.getProductDiscountedPrice(), retrievedProduct.getProductDiscountedPrice(), 0.0);
        assertEquals(product.getProductImages(), retrievedProduct.getProductImages());
    }

    @Test
    public void testGetProductDetails() {
        // Arrange
        User user = new User();
        user.setUserName("testuser");
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Test product");
        product.setProductDescription("Test product description");
        product.setProductActualPrice(100.0);
        product.setProductDiscountedPrice(90.0);
        List<Cart> carts = new ArrayList<>();
        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setProduct(product);
        cart.setUser(user);
        carts.add(cart);

        when(userDao.findById(JwtRequestFilter.CURRENT_USER)).thenReturn(Optional.of(user));
        when(cartDao.findByUser(user)).thenReturn(carts);

        // Act
        List<Product> result = productService.getProductDetails(false, -1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(product.getProductId(), result.get(0).getProductId());
        assertEquals(product.getProductName(), result.get(0).getProductName());
        assertEquals(product.getProductDescription(), result.get(0).getProductDescription());
        assertEquals(product.getProductActualPrice(), result.get(0).getProductActualPrice(), 0.001);
        assertEquals(product.getProductDiscountedPrice(), result.get(0).getProductDiscountedPrice(), 0.001);
    }
}
