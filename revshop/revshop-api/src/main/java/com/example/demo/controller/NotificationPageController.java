package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationPageController {

    @GetMapping("/notifications")
    public String showNotificationsPage() {
        return "notifications";
    }
}