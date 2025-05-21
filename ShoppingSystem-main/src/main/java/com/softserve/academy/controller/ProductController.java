package com.softserve.academy.controller;

import com.softserve.academy.model.Product;
import com.softserve.academy.model.ProductDTO;
import com.softserve.academy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // For validation if you use DTOs with validation annotations
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products") // Base path for all product-related endpoints
public class ProductController {

    private final ProductService productService;

//    @GetMapping("/")
//    public String index() {
//        return "index.html"; // Повертаємо ім'я файлу index.html
//    }
    @GetMapping("/")
    public RedirectView redirectToAddProduct() {
        return new RedirectView("/index.html");
    }

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
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) { // Додано ("id")
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all products
    // GET http://localhost:8080/api/products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Update an existing product
    // PUT http://localhost:8080/api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody Product productDetails) { // Додано ("id")
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product
    // DELETE http://localhost:8080/api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) { // Додано ("id")
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
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


    // НОВИЙ МЕТОД: Отримати продукти за ID категорії
    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getProductsByCategoryId(@PathVariable Long categoryId) {
        return productService.findByCategoryId(categoryId).stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setPrice(product.getPrice());
                    dto.setProducer(product.getProducer());
                    dto.setCountryOfOrigin(product.getCountryOfOrigin());
                    dto.setWeight(product.getWeight() != 0 ? product.getWeight() : 0); // Перетворення BigDecimal на Double
                    dto.setDescription(product.getDescription());
                    if (product.getCategory() != null) {
                        dto.setCategoryId(product.getCategory().getId());
                        dto.setCategoryName(product.getCategory().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Якщо ви використовуєте ProductDTO, то ваш існуючий метод getAllProducts() також має повертати List<ProductDTO>
    // Якщо у вас вже є ProductDTO, переконайтеся, що він включає всі необхідні поля та має геттери/сеттери
    // public List<ProductDTO> getAllProducts() { ... }
}
