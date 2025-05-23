package com.softserve.academy.repository;

import com.softserve.academy.model.Product;
import com.softserve.academy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategory(Category category);

    List<Product> findByProducerContainingIgnoreCase(String producer);

    List<Product> findByCategoryId(Long categoryId);
}
