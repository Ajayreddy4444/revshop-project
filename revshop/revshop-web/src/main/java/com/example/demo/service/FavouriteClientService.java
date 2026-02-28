package com.example.demo.service;

import com.example.demo.dto.FavouriteRequest;
import java.util.List;

public interface FavouriteClientService {

    String addToFavourites(FavouriteRequest request);

    String removeFromFavourites(Long userId, Long productId);

    List<Long> getUserFavouriteProductIds(Long userId);
}