package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name="orders")
public class Order {
	
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private long id;
	 
	 @ManyToOne
	 @JoinColumn(name="user_id",nullable=false)
	 private User user;
	 
	 @Column(nullable=false)
	 private LocalDateTime orderDate = LocalDateTime.now();
	 
	 @Column(nullable=false)
	 private Double totalAmount;
	 
	 @Column(nullable=false)
	 private String status;
	 
	 @OneToMany(mappedBy="order",cascade=CascadeType.ALL)
	 private List<OrderItem> items;
	 
	 public Order( ) {}

	 public long getId() {
		 return id;
	 }

	 public User getUser() {
		 return user;
	 }

	 public void setUser(User user) {
		 this.user = user;
	 }

	 public LocalDateTime getOrderDate() {
		 return orderDate;
	 }

	 public void setOrderDate(LocalDateTime orderDate) {
		 this.orderDate = orderDate;
	 }

	 public Double getTotalAmount() {
		 return totalAmount;
	 }

	 public void setTotalAmount(Double totalAmount) {
		 this.totalAmount = totalAmount;
	 }

	 public String getStatus() {
		 return status;
	 }

	 public void setStatus(String status) {
		 this.status = status;
	 }

	 public List<OrderItem> getItems() {
		 return items;
	 }

	 public void setItems(List<OrderItem> items) {
		 this.items = items;
	 }	 

}
