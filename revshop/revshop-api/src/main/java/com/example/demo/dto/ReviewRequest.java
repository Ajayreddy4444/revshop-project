package com.example.demo.dto;

public class ReviewRequest {

    private int rating;
    private String comment;
    private Long userId;

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}