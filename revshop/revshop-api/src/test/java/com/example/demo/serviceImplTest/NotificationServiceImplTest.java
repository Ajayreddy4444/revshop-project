package com.example.demo.serviceImplTest;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.serviceImpl.NotificationServiceImpl;

public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    public NotificationServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderNotification_success() {

        User user = new User();
        user.setId(1L);

        notificationService.createOrderNotification(
                user,
                10L,
                "Order placed successfully"
        );

        verify(notificationRepository, times(1))
                .save(any(Notification.class));
    }

    @Test
    void createLowStockNotification_success() {

        User seller = new User();
        seller.setId(2L);

        notificationService.createLowStockNotification(
                seller,
                "Low stock alert"
        );

        verify(notificationRepository, times(1))
                .save(any(Notification.class));
    }
}