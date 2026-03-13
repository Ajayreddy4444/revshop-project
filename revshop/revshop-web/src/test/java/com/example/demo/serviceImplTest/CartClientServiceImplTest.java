package com.example.demo.serviceImplTest;

import com.example.demo.dto.CartRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.serviceImpl.CartClientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartClientServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Inject baseUrl manually (because @Value is not loaded in unit tests)
        Field field = CartClientServiceImpl.class.getDeclaredField("baseUrl");
        field.setAccessible(true);
        field.set(service, "http://localhost:8080/auth");
    }

    @Test
    void add_ShouldCallPostForObject() {

        when(restTemplate.postForObject(anyString(), any(), eq(CartResponse.class)))
                .thenReturn(new CartResponse());

        CartResponse response = service.add(new CartRequest());

        assertNotNull(response);
        verify(restTemplate, times(1))
                .postForObject(anyString(), any(), eq(CartResponse.class));
    }

    @Test
    void getCart_ShouldReturnList() {

        CartResponse[] array = new CartResponse[]{new CartResponse()};

        when(restTemplate.getForEntity(anyString(), eq(CartResponse[].class)))
                .thenReturn(ResponseEntity.ok(array));

        List<CartResponse> result = service.getCart(1L);

        assertEquals(1, result.size());
        verify(restTemplate, times(1))
                .getForEntity(anyString(), eq(CartResponse[].class));
    }

    @Test
    void remove_ShouldCallDelete() {

        service.remove(1L);

        verify(restTemplate, times(1)).delete(anyString());
    }
}