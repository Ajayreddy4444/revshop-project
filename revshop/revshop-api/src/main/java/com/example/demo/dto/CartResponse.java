package com.example.demo.dto;

public class CartResponse {

    private Long itemId;
    private Long productId;
    private String productName; 
    private int quantity;

    public CartResponse(){}

    public CartResponse(Long itemId,
                        Long productId,
                        String productName,
                        int quantity){
        this.itemId = itemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}