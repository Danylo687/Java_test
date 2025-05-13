package com.softserve.academy.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_store")
@Data
public class ProductStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Integer stock;

    private Double price;

    /**
     * Increases the stock by a specified amount.
     * @param amount the amount to increase
     */
    public void increaseStock(int amount) {
        if (amount > 0) {
            this.stock += amount;
        }
    }

    /**
     * Decreases the stock by a specified amount.
     * @param amount the amount to decrease
     * @throws IllegalArgumentException if the amount exceeds the current stock
     */
    public void decreaseStock(int amount) {
        if (amount > this.stock) {
            throw new IllegalArgumentException("Insufficient stock to decrease");
        }
        this.stock -= amount;
    }

    /**
     * Updates the price of the product in the store.
     * @param newPrice the new price to set
     * @throws IllegalArgumentException if the new price is negative
     */
    public void updatePrice(double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = newPrice;
    }
}