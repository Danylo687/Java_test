package com.softserve.academy.controller;

import com.softserve.academy.model.Product;
import com.softserve.academy.model.ProductDTO; // Додано
import com.softserve.academy.service.ProductService;
import com.softserve.academy.repository.CategoryRepository; // Додано
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList; // Додано

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/")
    public RedirectView redirectToAddProduct() {
        return new RedirectView("/index.html");
    }

    @Autowired
    public ProductController(ProductService productService, CategoryRepository categoryRepository) { // ДОДАНО CategoryRepository
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody Product productDetails) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> findProductsByName(@RequestParam String name) {
        List<Product> products = productService.findByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDTO> getProductsByCategoryId(@PathVariable Long categoryId) {
        return productService.findProductDTOsByCategoryId(categoryId);
    }

    @PostMapping("/dto")
    public ResponseEntity<ProductDTO> createProductDTO(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO savedProductDTO = productService.saveProductDTO(productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<ProductDTO> getProductDTOById(@PathVariable("id") Long id) {
        return productService.getProductDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dto")
    public ResponseEntity<List<ProductDTO>> getAllProductDTOs() {
        List<ProductDTO> productDTOs = productService.getAllProductDTOs();
        return ResponseEntity.ok(productDTOs);
    }

    @PutMapping("/dto/{id}")
    public ResponseEntity<ProductDTO> updateProductDTO(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO productDetailsDTO) {
        try {
            ProductDTO updatedProductDTO = productService.updateProductDTO(id, productDetailsDTO);
            return ResponseEntity.ok(updatedProductDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}