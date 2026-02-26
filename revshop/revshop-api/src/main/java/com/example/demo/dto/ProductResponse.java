package com.example.demo.dto;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double mrp;
    private Integer quantity;
    private String imageUrl;
<<<<<<< Updated upstream
    private Integer lowStockThreshold;
=======
    private double averageRating;
    private int reviewCount;
>>>>>>> Stashed changes

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, String description,
                           Double price, Double mrp,
<<<<<<< Updated upstream
                           Integer quantity, String imageUrl, Integer lowStockThreshold) {
=======
                           Integer quantity, String imageUrl, Double averageRating, int reviewCount){
>>>>>>> Stashed changes
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.mrp = mrp;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
<<<<<<< Updated upstream
        this.lowStockThreshold = lowStockThreshold;
=======
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
>>>>>>> Stashed changes
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
<<<<<<< Updated upstream

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
=======
    
    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
>>>>>>> Stashed changes
    }
}
