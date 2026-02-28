package com.example.demo.serviceImpl;

import com.example.demo.dto.FavouriteRequest;
import com.example.demo.service.FavouriteClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FavouriteClientServiceImpl implements FavouriteClientService {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8080/api/favourites";

    @Override
    public String addToFavourites(FavouriteRequest request) {

        return restTemplate.postForObject(
                BASE_URL + "/add",
                request,
                String.class
        );
    }

    @Override
    public String removeFromFavourites(Long userId, Long productId) {

        restTemplate.delete(
                BASE_URL + "/remove?userId=" + userId +
                        "&productId=" + productId
        );

        return "Removed successfully";
    }

    @Override
    public List<Long> getUserFavouriteProductIds(Long userId) {

        List response = restTemplate.getForObject(
                BASE_URL + "/" + userId,
                List.class
        );

        List<Long> productIds = new ArrayList<>();

        if (response != null) {
            for (Object obj : response) {
                Map map = (Map) obj;
                productIds.add(
                        Long.valueOf(map.get("productId").toString())
                );
            }
        }

        return productIds;
    }
}