package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import java.util.Set;
import java.util.HashSet;
import java.util.List; // Додано
import java.util.ArrayList; // Додано

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String producer;

    private String CountryOfOrigin;

    private double weight;

    private String description;

    private BigDecimal price;

    // Додано: Поле для списку посилань на зображення
    // Використовуємо @ElementCollection для збереження списку примітивних типів
    // У базі даних це може бути окрема таблиця або JSON-масив у деяких БД (залежить від конфігурації JPA/Hibernate)
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default // Додано: Для Lombok Builder, щоб ініціалізувати поле за замовчуванням
    private List<String> imageUrls = new ArrayList<>(); // Додано: Ініціалізація, щоб уникнути NullPointerException


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "product_store",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    @JsonIgnore // Додано: Ігнорувати список магазинів при серіалізації продукту
    private Set<Store> stores = new HashSet<>();

    /**
     * Adds a store to the product's list of stores.
     * @param store the store to add
     */
    public void addStore(Store store) {
        stores.add(store);
        store.getProducts().add(this);
    }

    /**
     * Removes a store from the product's list of stores.
     * @param store the store to remove
     */
    public void removeStore(Store store) {
        stores.remove(store);
        store.getProducts().remove(this);
    }

    public String getCategoryName() {
        return (this.category != null) ? this.category.getName() : null;
    }
}