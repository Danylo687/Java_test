package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a store in the system.
 * Each store can have multiple products associated with it.
 */
@Entity
@Table(name = "store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Store name cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Location cannot be blank")
    private String location;

    @Pattern(regexp = "\\+?[0-9\\-\\s]{7,20}", message = "Invalid contact number format")
    private String contactNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Builder.Default
    @ManyToMany(mappedBy = "stores", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    /**
     * Adds a product to the store's list of products.
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
        product.getStores().add(this);
    }

    /**
     * Removes a product from the store's list of products.
     * @param product the product to remove
     */
    public void removeProduct(Product product) {
        products.remove(product);
        product.getStores().remove(this);
    }

    /**
     * Retrieves all products in the store.
     * @return the set of products
     */
    public Set<Product> getProducts() {
        return products;
    }
}