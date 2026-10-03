package com.babyshophub.controller;

import com.babyshophub.entity.Category;
import com.babyshophub.entity.Product;
import com.babyshophub.entity.User;
import com.babyshophub.repository.UserRepository;
import com.babyshophub.service.CategoryService;
import com.babyshophub.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // Secures all endpoints in this controller to Admins only
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public AdminController(ProductService productService, CategoryService categoryService, UserRepository userRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    // Admin: Create product with optional image upload
    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") Product product,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("brandId") Long brandId,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = productService.saveImageLocally(imageFile);
                if (product.getImageUrls() != null) {
                    product.getImageUrls().add(imageUrl);
                }
            }

            Product savedProduct = productService.createProduct(product, categoryId, brandId);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Admin: Add a new category
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    // Admin: Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(allUsers);
    }
}