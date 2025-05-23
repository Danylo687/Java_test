package com.softserve.academy.model;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String producer;
    private String countryOfOrigin;
    private Double weight;
    private String description;
    private Long categoryId;
    private String categoryName;
    private List<String> imageUrls; // Додано: Список посилань на зображення

    // Конструктор без аргументів
    public ProductDTO() {
    }

    // Конструктор з усіма аргументами (можете створити, якщо потрібно)
    public ProductDTO(Long id, String name, BigDecimal price, String producer,
                      String countryOfOrigin, Double weight, String description,
                      Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.countryOfOrigin = countryOfOrigin;
        this.weight = weight;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Гетери
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProducer() {
        return producer;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public Double getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    // Сетери
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    // Також можуть бути додані методи equals(), hashCode(), toString()
}