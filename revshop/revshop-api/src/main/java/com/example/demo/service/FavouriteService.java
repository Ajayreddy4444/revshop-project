package com.example.demo.service;

import com.example.demo.entity.Favourite;
import java.util.List;

public interface FavouriteService {

    void addToFavourites(Long userId, Long productId);

    void removeFromFavourites(Long userId, Long productId);

    List<Favourite> getUserFavourites(Long userId);
}