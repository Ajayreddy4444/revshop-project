package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;

import java.util.List;

public interface NotificationService {

    // ✅ Create order notification for buyer
    void createOrderNotification(User user, Long orderId, String address);

    // ✅ Create order notification for seller (normal order)
    void createSellerOrderNotification(User seller, Long orderId, String productName);

    // ✅ Create low-stock notification for seller
    void createLowStockNotification(User seller, String message);

    // ✅ Get all notifications of a user
    List<Notification> getUserNotifications(User user);

    // ✅ Get unread count
    long getUnreadCount(User user);

    // ✅ Mark single notification as seen
    void markAsSeen(Long notificationId);

    // ✅ Mark all notifications as seen
    void markAllAsSeen(User user);
}