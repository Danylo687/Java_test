package com.softserve.academy.repository;

import com.softserve.academy.model.Product;
import com.softserve.academy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by name (case-insensitive search)
    List<Product> findByNameContainingIgnoreCase(String name);


    // Find products by category
    List<Product> findByCategory(Category category);

    // Find products by producer
    List<Product> findByProducerContainingIgnoreCase(String producer);

    // You can add more custom query methods here as needed
    // For example, find products within a certain price range, etc.
}
