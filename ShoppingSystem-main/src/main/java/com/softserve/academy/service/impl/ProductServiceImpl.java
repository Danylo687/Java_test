package com.softserve.academy.service.impl;

import com.softserve.academy.model.Product;
import com.softserve.academy.repository.ProductRepository; // Assuming you create this repository
import com.softserve.academy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Good practice to have transactions at the service layer
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    // You might also need CategoryRepository if you are setting category within product service
    // private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository /*, CategoryRepository categoryRepository */) {
        this.productRepository = productRepository;
        // this.categoryRepository = categoryRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        // Add any business logic before saving, e.g., validation, setting defaults
        // If category is passed by ID, you might fetch it here:
        // product.setCategory(categoryRepository.findById(product.getCategory().getId()).orElse(null));
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true) // readOnly for query methods
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    // У ProductServiceImpl -> updateProduct
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) { // Для BigDecimal/Double перевіряйте на null
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getProducer() != null) {
            product.setProducer(productDetails.getProducer());
        }
        // ... і так далі для countryOfOrigin, weight, category
        if (productDetails.getCategory() != null /* && productDetails.getCategory().getId() != null */) {
            // Логіка оновлення категорії
            product.setCategory(productDetails.getCategory());
        } else {
            // Якщо productDetails.getCategory() is null, чи потрібно встановлювати null в product.setCategory?
            // Можливо, якщо category не передано, його не слід змінювати.
        }
        // ...
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id); // Or a custom exception
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByName(String name) {
        // Assuming ProductRepository has a findByNameContainingIgnoreCase method or similar
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}