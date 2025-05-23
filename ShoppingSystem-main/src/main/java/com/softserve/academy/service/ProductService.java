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

    public List<Product> findByCategoryId(Long categoryId);

    ProductDTO saveProductDTO(ProductDTO productDTO);
    Optional<ProductDTO> getProductDTOById(Long id);
    List<ProductDTO> getAllProductDTOs();
    ProductDTO updateProductDTO(Long id, ProductDTO productDTO);
    List<ProductDTO> findProductDTOsByCategoryId(Long categoryId);
}