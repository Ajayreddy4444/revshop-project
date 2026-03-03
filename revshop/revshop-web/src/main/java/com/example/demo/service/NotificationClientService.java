package com.example.demo.service;

import com.example.demo.dto.NotificationDto;
import java.util.List;

public interface NotificationClientService {

    List<NotificationDto> getAllNotifications(Long userId);

    void markAsRead(Long id);
}