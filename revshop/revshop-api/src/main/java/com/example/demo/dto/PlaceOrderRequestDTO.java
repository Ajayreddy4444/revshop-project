package com.example.demo.dto;

public class PlaceOrderRequestDTO {

    private Long userId;
    private Long addressId;

    public PlaceOrderRequestDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

	
}