package com.example.demo.serviceImplTest;

import com.example.demo.dto.WishlistResponse;
import com.example.demo.serviceImpl.WishlistClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WishlistClientServiceImpl wishlistClientService;

    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(
                wishlistClientService,
                "backendBaseUrl",
                BASE_URL
        );
    }

    @Test
    void toggleWishlist_shouldReturnTrue_whenAdded() {

        WishlistResponse response = new WishlistResponse(2L, true);

        when(restTemplate.postForObject(
                eq(BASE_URL + "/wishlist/toggle/{userId}/{productId}"),
                isNull(),
                eq(WishlistResponse.class),
                eq(1L),
                eq(2L)
        )).thenReturn(response);

        boolean result = wishlistClientService.toggleWishlist(1L, 2L);

        assertTrue(result);

        verify(restTemplate).postForObject(
                eq(BASE_URL + "/wishlist/toggle/{userId}/{productId}"),
                isNull(),
                eq(WishlistResponse.class),
                eq(1L),
                eq(2L)
        );
    }

    @Test
    void toggleWishlist_shouldReturnFalse_whenResponseNull() {

        when(restTemplate.postForObject(
                anyString(),
                isNull(),
                eq(WishlistResponse.class),
                anyLong(),
                anyLong()
        )).thenReturn(null);

        boolean result = wishlistClientService.toggleWishlist(1L, 2L);

        assertFalse(result);
    }

    @Test
    void getWishlistProductIds_shouldReturnList() {

        List<Long> mockList = List.of(10L, 20L);

        ResponseEntity<List<Long>> response =
                new ResponseEntity<>(mockList, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(BASE_URL + "/wishlist/my/{userId}"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class),
                eq(1L)
        )).thenReturn(response);

        List<Long> result = wishlistClientService.getWishlistProductIds(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(10L));
    }

    @Test
    void removeFromWishlist_shouldCallDelete() {

        wishlistClientService.removeFromWishlist(1L, 2L);

        verify(restTemplate).delete(
                BASE_URL + "/wishlist/remove/{userId}/{productId}",
                1L,
                2L
        );
    }
}