package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;

import java.util.List;

public interface NotificationService {

    // Create order notification
    void createOrderNotification(User user, Long orderId, String address);

    // Get all notifications of a user
    List<Notification> getUserNotifications(User user);

    // Get unread count
    long getUnreadCount(User user);

    // Mark notification as seen
    void markAsSeen(Long notificationId);
}