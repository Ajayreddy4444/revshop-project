package com.example.demo.serviceImpl;

import com.example.demo.entity.Favourite;
import com.example.demo.repository.FavouriteRepository;
import com.example.demo.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavouriteServiceImpl implements FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Override
    public void addToFavourites(Long userId, Long productId) {

        Optional<Favourite> existing =
                favouriteRepository.findByUserIdAndProductId(userId, productId);

        if (existing.isEmpty()) {
            Favourite favourite = new Favourite();
            favourite.setUserId(userId);
            favourite.setProductId(productId);
            favouriteRepository.save(favourite);
        }
    }

    @Override
    public void removeFromFavourites(Long userId, Long productId) {
        favouriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<Favourite> getUserFavourites(Long userId) {
        return favouriteRepository.findByUserId(userId);
    }
}