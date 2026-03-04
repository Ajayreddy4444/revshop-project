package com.example.demo.serviceImplTest;

import com.example.demo.entity.Wishlist;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.serviceImpl.WishlistServiceImpl;
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
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    void toggleWishlist_shouldAddProduct_whenNotExists() {

        when(wishlistRepository.findByUserIdAndProductId(1L, 2L))
                .thenReturn(Optional.empty());

        boolean result = wishlistService.toggleWishlist(1L, 2L);

        assertTrue(result);
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    void toggleWishlist_shouldRemoveProduct_whenExists() {

        Wishlist wishlist = new Wishlist(1L, 2L);

        when(wishlistRepository.findByUserIdAndProductId(1L, 2L))
                .thenReturn(Optional.of(wishlist));

        boolean result = wishlistService.toggleWishlist(1L, 2L);

        assertFalse(result);
        verify(wishlistRepository).delete(wishlist);
    }

    @Test
    void getUserWishlistProductIds_shouldReturnProductIds() {

        Wishlist w1 = new Wishlist(1L, 10L);
        Wishlist w2 = new Wishlist(1L, 20L);

        when(wishlistRepository.findByUserId(1L))
                .thenReturn(List.of(w1, w2));

        List<Long> result = wishlistService.getUserWishlistProductIds(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(10L));
        assertTrue(result.contains(20L));
    }

    @Test
    void isWishlisted_shouldReturnTrue_whenExists() {

        when(wishlistRepository.findByUserIdAndProductId(1L, 2L))
                .thenReturn(Optional.of(new Wishlist(1L, 2L)));

        boolean result = wishlistService.isWishlisted(1L, 2L);

        assertTrue(result);
    }

    @Test
    void isWishlisted_shouldReturnFalse_whenNotExists() {

        when(wishlistRepository.findByUserIdAndProductId(1L, 2L))
                .thenReturn(Optional.empty());

        boolean result = wishlistService.isWishlisted(1L, 2L);

        assertFalse(result);
    }

    @Test
    void removeFromWishlist_shouldDelete_whenExists() {

        Wishlist wishlist = new Wishlist(1L, 2L);

        when(wishlistRepository.findByUserIdAndProductId(1L, 2L))
                .thenReturn(Optional.of(wishlist));

        wishlistService.removeFromWishlist(1L, 2L);

        verify(wishlistRepository).delete(wishlist);
    }

    @Test
    void removeFromWishlist_shouldDoNothing_whenNotExists() {

        when(wishlistRepository.findByUserIdAndProductId(1L, 2L))
                .thenReturn(Optional.empty());

        wishlistService.removeFromWishlist(1L, 2L);

        verify(wishlistRepository, never()).delete(any());
    }
}
