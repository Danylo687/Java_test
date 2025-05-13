package com.softserve.academy.controller;

import com.softserve.academy.model.Product;
import com.softserve.academy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // For validation if you use DTOs with validation annotations
import java.util.List;

@RestController
@RequestMapping("/api/products") // Base path for all product-related endpoints
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product
    // POST http://localhost:8080/api/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        // @Valid annotation triggers validation if Product has JSR 303 annotations
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Get a single product by ID
    // GET http://localhost:8080/api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok) // If product exists, wrap in ResponseEntity.ok()
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404
    }

    // Get all products
    // GET http://localhost:8080/api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products); // Завжди повертаємо статус 200 OK зі списком (може бути порожнім)
    }

    // Update an existing product
    // PUT http://localhost:8080/api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) { // Catch specific exceptions like ResourceNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product
    // DELETE http://localhost:8080/api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 on successful deletion
        } catch (RuntimeException e) { // Catch specific exceptions
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Example: Find products by name
    // GET http://localhost:8080/api/products/search?name=Smartphone
    @GetMapping("/search")
    public ResponseEntity<List<Product>> findProductsByName(@RequestParam String name) {
        List<Product> products = productService.findByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }
}
