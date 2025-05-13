package com.softserve.academy.service;

import com.softserve.academy.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product saveProduct(Product product);
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
    List<Product> findByName(String name);
    // Add other business logic methods as needed
}