package com.example.demo.dto;

public class CartResponse {

    private Long itemId;
    private Long productId;
    private String productName;
    private String imageUrl;

    private Double price;
    private Double mrp;

    private int quantity;

    public CartResponse(){}

    public CartResponse(Long itemId,
                        Long productId,
                        String productName,
                        String imageUrl,
                        Double price,
                        Double mrp,
                        int quantity){

        this.itemId = itemId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.mrp = mrp;
        this.quantity = quantity;
    }

    public Long getItemId() { return itemId; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getImageUrl() { return imageUrl; }
    public Double getPrice() { return price; }
    public Double getMrp() { return mrp; }
    public int getQuantity() { return quantity; }
}