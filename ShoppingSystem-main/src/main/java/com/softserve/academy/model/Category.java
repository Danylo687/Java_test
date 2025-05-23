package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    /**
     * Adds a product to the category.
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    /**
     * Removes a product from the category.
     * @param product the product to remove
     */
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }

    /**
     * Retrieves all products in the category.
     * @return the list of products
     */
    @JsonIgnore
    public List<Product> getProducts() {
        return products;
    }
}