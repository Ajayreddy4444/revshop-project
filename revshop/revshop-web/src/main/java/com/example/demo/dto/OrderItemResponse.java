package com.example.demo.dto;

public class OrderItemResponse {
	
	
	private Long productId;
	private String productName;
	private Integer quantity;
	private Double priceAtPurchase;
	private Double subtotal;
	private String imageUrl;
	

	public OrderItemResponse() {}

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
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

}
