package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem,Long> {
 
	
	List<OrderItem> findByProduct_Seller_Id(Long sellerId);
}
