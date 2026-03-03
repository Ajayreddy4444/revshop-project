package com.example.demo.serviceImplTest;

import com.example.demo.dto.Category;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductRequest;
import com.example.demo.serviceImpl.ProductClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductClientServiceImpl productClientService;

    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setup() {
        // Manually inject baseUrl
        ReflectionTestUtils.setField(productClientService, "baseUrl", BASE_URL);
    }

    @Test
    void getAllProducts_shouldReturnList() {

        List<ProductDto> mockList = List.of(new ProductDto());

        ResponseEntity<List<ProductDto>> response =
                new ResponseEntity<>(mockList, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BASE_URL + "/products"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        List<ProductDto> result = productClientService.getAllProducts();

        assertEquals(1, result.size());
        verify(restTemplate).exchange(
                eq(BASE_URL + "/products"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class));
    }

    @Test
    void getProductById_shouldReturnProduct() {

        ProductDto dto = new ProductDto();

        when(restTemplate.getForObject(
                BASE_URL + "/products/1",
                ProductDto.class))
                .thenReturn(dto);

        ProductDto result = productClientService.getProductById(1L);

        assertNotNull(result);
        verify(restTemplate).getForObject(
                BASE_URL + "/products/1",
                ProductDto.class);
    }

    @Test
    void createProduct_shouldCallPost() {

        ProductRequest request = new ProductRequest();

        productClientService.createProduct(request, 2L);

        verify(restTemplate).postForEntity(
                BASE_URL + "/products?sellerId=2",
                request,
                Void.class);
    }

    @Test
    void getAllCategories_shouldReturnList() {

        Category[] categories = {new Category(), new Category()};

        ResponseEntity<Category[]> response =
                new ResponseEntity<>(categories, HttpStatus.OK);

        when(restTemplate.getForEntity(
                BASE_URL + "/categories",
                Category[].class))
                .thenReturn(response);

        List<Category> result = productClientService.getAllCategories();

        assertEquals(2, result.size());
    }

    @Test
    void updateProduct_shouldCallPut() {

        ProductRequest request = new ProductRequest();

        productClientService.updateProduct(1L, request);

        verify(restTemplate).put(
                BASE_URL + "/products/1",
                request);
    }

    @Test
    void updateStock_shouldCallPatch() {

        productClientService.updateStock(1L, 10);

        verify(restTemplate).patchForObject(
                BASE_URL + "/products/1/stock?quantity=10",
                null,
                Void.class);
    }

    @Test
    void deleteProduct_shouldCallDelete() {

        productClientService.deleteProduct(1L);

        verify(restTemplate).delete(
                BASE_URL + "/products/1");
    }

    @Test
    void createCategory_shouldReturnCategory() {

        Category category = new Category();

        ResponseEntity<Category> response =
                new ResponseEntity<>(category, HttpStatus.OK);

        when(restTemplate.postForEntity(
                eq(BASE_URL + "/categories"),
                any(HttpEntity.class),
                eq(Category.class)))
                .thenReturn(response);

        Category result = productClientService.createCategory("Test", "Desc");

        assertNotNull(result);
    }

    @Test
    void createCategory_shouldThrowRuntimeException_onHttpError() {

        when(restTemplate.postForEntity(
                eq(BASE_URL + "/categories"),
                any(HttpEntity.class),
                eq(Category.class)))
                .thenThrow(HttpClientErrorException.BadRequest.class);

        assertThrows(RuntimeException.class,
                () -> productClientService.createCategory("Test", "Desc"));
    }
}