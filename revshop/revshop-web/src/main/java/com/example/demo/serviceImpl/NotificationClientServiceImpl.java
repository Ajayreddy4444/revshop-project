package com.example.demo.serviceImpl;

import com.example.demo.dto.NotificationDto;
import com.example.demo.service.NotificationClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class NotificationClientServiceImpl implements NotificationClientService {

    private static final String BASE_URL = "http://localhost:8080/api/notifications";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<NotificationDto> getAllNotifications(Long userId) {

        String url = BASE_URL + "/" + userId;

        ResponseEntity<NotificationDto[]> response =
                restTemplate.getForEntity(url, NotificationDto[].class);

        return Arrays.asList(response.getBody());
    }

    @Override
    public void markAsRead(Long id) {

        String url = BASE_URL + "/seen/" + id;

        restTemplate.put(url, null);
    }
}