package com.example.demo.serviceImpl;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.ProductService;
import com.example.demo.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final WishlistRepository wishlistRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              UserRepository userRepository,
                              OrderItemRepository orderItemRepository,
                              CartItemRepository cartItemRepository,
                              WishlistRepository wishlistRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return mapToDto(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest, Long sellerId) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setMrp(productRequest.getMrp());
        product.setQuantity(productRequest.getQuantity());
        product.setLowStockThreshold(productRequest.getLowStockThreshold());
        product.setImageUrl(productRequest.getImageUrl());
        product.setActive(true);
        product.setCategory(category);
        product.setSeller(seller);
        product.setCreatedAt(java.time.LocalDateTime.now());
        product.setMrp(productRequest.getMrp());
        product.setDiscountPercent(productRequest.getDiscountPercent());

        if (product.getMrp() != null) {
            double discount = product.getDiscountPercent() == null ? 0 : product.getDiscountPercent();

            if (discount < 0 || discount > 100) {
                throw new BadRequestException("Discount must be between 0 and 100");
            }

            double finalPrice = product.getMrp() - (product.getMrp() * discount / 100);
            product.setPrice(finalPrice);
        }

        boolean exists = productRepository
                .existsBySellerIdAndNameIgnoreCaseAndCategory_Id(
                        sellerId,
                        productRequest.getName(),
                        productRequest.getCategoryId()
                );

        if (exists) {
            throw new RuntimeException("You already added this product in this category");
        }

        productRepository.save(product);

        return mapToDto(product);
    }

    @Override
    public List<ProductResponse> getProductsBySeller(Long sellerId) {
        return productRepository.findBySellerId(sellerId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new BadRequestException("Quantity must be >= 0");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setLowStockThreshold(request.getLowStockThreshold());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
        product.setMrp(request.getMrp());
        product.setDiscountPercent(request.getDiscountPercent());

        if (product.getMrp() != null) {
            double discount = product.getDiscountPercent() == null ? 0 : product.getDiscountPercent();

            if (discount < 0 || discount > 100) {
                throw new BadRequestException("Discount must be between 0 and 100");
            }

            double finalPrice = product.getMrp() - (product.getMrp() * discount / 100);
            product.setPrice(finalPrice);
        }

        productRepository.save(product);

        return mapToDto(product);
    }

    @Override
    public ProductResponse updateStock(Long id, Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (quantity < 0) {
            throw new BadRequestException("Quantity must be >= 0");
        }

        product.setQuantity(quantity);

        productRepository.save(product);

        return mapToDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean hasOrders =
                orderItemRepository.existsByProductId(id);

        boolean inCart =
                cartItemRepository.existsByProductId(id);

        boolean inWishlist =
                wishlistRepository.existsByProductId(id);
        if (hasOrders) {

            product.setActive(false);
            productRepository.save(product);

        } else {

            if (inCart) {
                cartItemRepository.deleteByProductId(id);
            }

            if (inWishlist) {
                wishlistRepository.deleteByProductId(id);
            }

            productRepository.delete(product);
        }
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword,
                                                Long categoryId,
                                                Double minPrice,
                                                Double maxPrice) {

        Specification<Product> spec =
                ProductSpecification.filter(keyword, categoryId, minPrice, maxPrice);

        return productRepository.findAll(spec)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ProductResponse mapToDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMrp(),
                product.getQuantity(),
                product.getImageUrl(),
                product.getLowStockThreshold(),
                product.getAverageRating(),
                product.getReviewCount(),
                product.getDiscountPercent(),
                product.getActive()
        );
    }
}
