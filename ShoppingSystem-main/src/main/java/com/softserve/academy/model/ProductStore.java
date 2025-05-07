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
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private Integer stock;

    private Double price;
}