package com.example.demo.serviceImpl;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // ================================
    // ✅ Buyer Notification
    // ================================
    @Override
    public void createOrderNotification(User user, Long orderId, String address) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Order Placed Successfully");
        notification.setMessage("Order ID: " + orderId + " | Shipping Address: " + address);
        notification.setType("order"); // normal order
        notificationRepository.save(notification);
    }

    // ================================
    // ✅ Seller Notification (normal order)
    // ================================
    @Override
    public void createSellerOrderNotification(User seller, Long orderId, String productName) {
        Notification notification = new Notification();
        notification.setUser(seller);
        notification.setTitle("New Order for Your Product");
        notification.setMessage("Order ID: " + orderId + " | Product: " + productName);
        notification.setType("order"); // normal order
        notificationRepository.save(notification);
    }

    // ================================
    // ✅ Low Stock Notification for Seller
    // ================================
    @Override
    public void createLowStockNotification(User seller, String message) {
        Notification notification = new Notification();
        notification.setUser(seller);
        notification.setTitle("⚠️ Low Stock Alert");
        notification.setMessage(message); // only the low-stock text
        notification.setType("warning"); // mark type as warning
        notificationRepository.save(notification);
    }

    // ================================
    // ✅ Get all notifications for a user
    // ================================
    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // ================================
    // ✅ Get unread count
    // ================================
    @Override
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndSeenFalse(user);
    }

    // ================================
    // ✅ Mark single notification as seen
    // ================================
    @Override
    public void markAsSeen(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setSeen(true);
            notificationRepository.save(notification);
        }
    }

    // ================================
    // ✅ Mark all notifications as seen
    // ================================
    @Override
    public void markAllAsSeen(User user) {
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        for (Notification notification : notifications) {
            if (!notification.isSeen()) {
                notification.setSeen(true);
            }
        }
        notificationRepository.saveAll(notifications);
    }
}