package com.example.demo.config;


import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(CategoryRepository categoryRepository,
                               ProductRepository productRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category electronics = new Category();
                electronics.setName("Electronics");
                electronics.setDescription("Electronics items");
                electronics.setCreatedAt(LocalDateTime.now());

                Category books = new Category();
                books.setName("Books");
                books.setDescription("All kinds of books");
                books.setCreatedAt(LocalDateTime.now());

                categoryRepository.save(electronics);
                categoryRepository.save(books);

                if (productRepository.count() == 0) {

                    Product phone = new Product();
                    phone.setName("iPhone 14");
                    phone.setDescription("Apple smartphone with A15 chip");
                    phone.setPrice(80000.0);
                    phone.setMrp(85000.0);
                    phone.setQuantity(10);
                    phone.setLowStockThreshold(2);
                    phone.setImageUrl("iphone14.png");
                    phone.setActive(true);
                    phone.setCategory(electronics);
                    phone.setCreatedAt(LocalDateTime.now());

                    Product javaBook = new Product();
                    javaBook.setName("Java Programming");
                    javaBook.setDescription("Complete guide to Java");
                    javaBook.setPrice(500.0);
                    javaBook.setMrp(600.0);
                    javaBook.setQuantity(25);
                    javaBook.setLowStockThreshold(5);
                    javaBook.setImageUrl("java-book.png");
                    javaBook.setActive(true);
                    javaBook.setCategory(books);
                    javaBook.setCreatedAt(LocalDateTime.now());

                    productRepository.save(phone);
                    productRepository.save(javaBook);
                }

            }
        };
    }
}
