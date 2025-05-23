package com.softserve.academy.service.impl;

import com.softserve.academy.model.Category;
import com.softserve.academy.model.Product;
import com.softserve.academy.model.ProductDTO;
import com.softserve.academy.repository.CategoryRepository;
import com.softserve.academy.repository.ProductRepository;
import com.softserve.academy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setProducer(productDetails.getProducer());
                    existingProduct.setCountryOfOrigin(productDetails.getCountryOfOrigin());
                    existingProduct.setWeight(productDetails.getWeight());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public ProductDTO saveProductDTO(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Override
    public Optional<ProductDTO> getProductDTOById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<ProductDTO> getAllProductDTOs() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProductDTO(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setProducer(productDTO.getProducer());
        existingProduct.setCountryOfOrigin(productDTO.getCountryOfOrigin());
        existingProduct.setWeight(productDTO.getWeight() != null ? productDTO.getWeight() : 0.0);
        existingProduct.setDescription(productDTO.getDescription());

        if (productDTO.getCategoryId() != null) {
            categoryRepository.findById(productDTO.getCategoryId())
                    .ifPresent(existingProduct::setCategory);
        } else {
            existingProduct.setCategory(null);
        }

        if (productDTO.getImageUrls() != null) {
            existingProduct.setImageUrls(new ArrayList<>(productDTO.getImageUrls()));
        } else {
            existingProduct.setImageUrls(new ArrayList<>());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }

    @Override
    public List<ProductDTO> findProductDTOsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setProducer(product.getProducer());
        dto.setCountryOfOrigin(product.getCountryOfOrigin());
        dto.setWeight(product.getWeight());
        dto.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getImageUrls() != null) {
            dto.setImageUrls(new ArrayList<>(product.getImageUrls()));
        } else {
            dto.setImageUrls(new ArrayList<>());
        }
        return dto;
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setProducer(productDTO.getProducer());
        product.setCountryOfOrigin(productDTO.getCountryOfOrigin());
        product.setWeight(productDTO.getWeight() != null ? productDTO.getWeight() : 0.0);
        product.setDescription(productDTO.getDescription());

        if (productDTO.getCategoryId() != null) {
            categoryRepository.findById(productDTO.getCategoryId())
                    .ifPresent(product::setCategory);
        }

        if (productDTO.getImageUrls() != null) {
            product.setImageUrls(new ArrayList<>(productDTO.getImageUrls()));
        } else {
            product.setImageUrls(new ArrayList<>());
        }
        return product;
    }
}