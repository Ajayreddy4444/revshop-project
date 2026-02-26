package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:8081")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserRepository userRepository;

	// Get all notifications of a user
	@GetMapping("/{userId}")
	public List<Notification> getNotifications(@PathVariable Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		return notificationService.getUserNotifications(user);
	}

	// Get unread count
	@GetMapping("/unread/{userId}")
	public long getUnread(@PathVariable Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		return notificationService.getUnreadCount(user);
	}

	// Mark notification as seen
	@PutMapping("/seen/{id}")
	public void markSeen(@PathVariable Long id) {
		notificationService.markAsSeen(id);
	}

	// ✅ NEW: Mark ALL notifications as seen
	@PutMapping("/markAllSeen/{userId}")
	public void markAllSeen(@PathVariable Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		notificationService.markAllAsSeen(user);
	}
}