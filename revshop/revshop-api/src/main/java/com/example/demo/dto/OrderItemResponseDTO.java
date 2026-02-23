package com.example.demo.dto;

public class OrderItemResponseDTO {

	
	private String  productName;
	private Integer quantity;
	private Double priceAtPurchase;
	private Double subtotal;
	
	public OrderItemResponseDTO() {
		
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPriceAtPurchase() {
		return priceAtPurchase;
	}

	public void setPriceAtPurchase(Double priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
}


