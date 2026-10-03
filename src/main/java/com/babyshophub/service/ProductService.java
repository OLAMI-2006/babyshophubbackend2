package com.babyshophub.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.babyshophub.entity.Brand;
import com.babyshophub.entity.Category;
import com.babyshophub.entity.Product;
import com.babyshophub.repository.BrandRepository;
import com.babyshophub.repository.CategoryRepository;
import com.babyshophub.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    
    private final String UPLOAD_DIR = "uploads/products/";

    public ProductService(ProductRepository productRepository, 
                          CategoryRepository categoryRepository, 
                          BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    public String saveImageLocally(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/products/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image file: " + e.getMessage());
        }
    }

    public Product createProduct(Product product, Long categoryId, Long brandId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
                
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + brandId));

        product.setCategory(category);
        product.setBrand(brand);
        
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    public Page<Product> getFilteredProducts(String keyword, Long categoryId, Long brandId, Pageable pageable) {
        return productRepository.searchAndFilterProducts(keyword, categoryId, brandId, pageable);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    
}