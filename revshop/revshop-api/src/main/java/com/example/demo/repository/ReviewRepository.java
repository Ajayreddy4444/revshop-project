package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 🔹 Get all reviews of a product (for rating calculation)
    List<Review> findByProduct(Product product);

    // 🔹 Pagination support (IMPORTANT)
    Page<Review> findByProduct(Product product, Pageable pageable);

    // 🔹 Prevent duplicate reviews
    Optional<Review> findByUserAndProduct(User user, Product product);

    // 🔹 Optional: count reviews for stats
    long countByProduct(Product product);
    
    //existing review not allowed
    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);
}