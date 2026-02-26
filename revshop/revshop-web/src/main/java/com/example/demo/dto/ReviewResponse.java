package com.example.demo.dto;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;
    private Long userId;
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    // 🔹 No-args constructor (IMPORTANT)
    public ReviewResponse() {
    }

    // 🔹 All-args constructor (optional)
    public ReviewResponse(Long id, Long userId, String userName,
                          int rating, String comment,
                          LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // 🔹 Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}