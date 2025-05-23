package com.softserve.academy.service;

import com.softserve.academy.model.Product;
import com.softserve.academy.model.ProductDTO;

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

    // НОВИЙ МЕТОД: Знайти продукти за ID категорії
    public List<Product> findByCategoryId(Long categoryId);

    // ДОДАНО: Нові методи для роботи з ProductDTO
    ProductDTO saveProductDTO(ProductDTO productDTO);
    Optional<ProductDTO> getProductDTOById(Long id);
    List<ProductDTO> getAllProductDTOs();
    ProductDTO updateProductDTO(Long id, ProductDTO productDTO);
    List<ProductDTO> findProductDTOsByCategoryId(Long categoryId);
    // Можливо, також знадобиться метод deleteProduct, який приймає ID, він вже є.
}