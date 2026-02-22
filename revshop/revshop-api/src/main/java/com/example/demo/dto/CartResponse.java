package com.example.demo.dto;

public class CartResponse {

    private Long itemId;
    private Long productId;
    private int quantity;

    public CartResponse(){}

    public CartResponse(Long itemId, Long productId, int quantity){
        this.itemId = itemId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getItemId() { return itemId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
}
