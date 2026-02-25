package com.example.demo.dto;

public class PlaceOrderRequestDTO {

	private Long userId;
	private paymentMethod paymentMethod;

	
	
	private Long addressId;

	public PlaceOrderRequestDTO() {
		
	}
	public Long getAddressId() {
	    return addressId;
	}

	public void setAddressId(Long addressId) {
	    this.addressId = addressId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public  PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(  PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


}