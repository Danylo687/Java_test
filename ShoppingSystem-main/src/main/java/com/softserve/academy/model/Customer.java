package com.softserve.academy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customer") // Задаємо назву таблиці в базі даних
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "\\+?[0-9\\-\\s]{7,20}", message = "Invalid contact number format")
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore // Додано: Ігнорувати список покупок при серіалізації клієнта
    private List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore // Додано: Ігнорувати список покупок при серіалізації клієнта
    private List<Purchase> favorites = new ArrayList<>();

    /**
     * Adds a purchase to the customer's list of purchases.
     * @param purchase the purchase to add
     */
    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
        purchase.setCustomer(this);
    }

    /**
     * Removes a purchase from the customer's list of purchases.
     * @param purchase the purchase to remove
     */
    public void removePurchase(Purchase purchase) {
        purchases.remove(purchase);
        purchase.setCustomer(null);
    }

    /**
     * Adds a product to the customer's list of favorites.
     * @param favorite the product to add
     */
    public void addFavorite(Purchase favorite) {
        favorites.add(favorite);
        favorite.setCustomer(this);
    }

    /**
     * Removes a product from the customer's list of favorites.
     * @param favorite the product to remove
     */
    public void removeFavorite(Purchase favorite) {
        favorites.remove(favorite);
        favorite.setCustomer(null);
    }
}
