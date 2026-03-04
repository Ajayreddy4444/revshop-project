package com.example.demo.serviceImplTest;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductById_shouldReturnProduct() {

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setActive(true);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertEquals("Laptop", response.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_shouldThrowException_whenNotFound() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }

    @Test
    void createProduct_shouldCreateSuccessfully() {

        ProductRequest request = new ProductRequest();
        request.setName("Phone");
        request.setCategoryId(1L);
        request.setMrp(1000.0);
        request.setDiscountPercent(10.0);

        Category category = new Category();
        User seller = new User();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(seller));

        when(productRepository.existsBySellerIdAndNameIgnoreCaseAndCategory_Id(
                anyLong(), anyString(), anyLong()))
                .thenReturn(false);

        ProductResponse response = productService.createProduct(request, 2L);

        assertEquals("Phone", response.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateStock_shouldUpdateQuantity() {

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(5);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse response = productService.updateStock(1L, 10);

        assertEquals(10, response.getQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void updateStock_shouldThrowException_whenNegative() {

        Product product = new Product();
        product.setId(1L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class,
                () -> productService.updateStock(1L, -1));
    }

    @Test
    void deleteProduct_shouldSoftDelete_whenHasOrders() {

        Product product = new Product();
        product.setId(1L);
        product.setActive(true);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderItemRepository.existsByProductId(1L))
                .thenReturn(true);

        productService.deleteProduct(1L);

        assertFalse(product.getActive());
        verify(productRepository).save(product);
    }
}