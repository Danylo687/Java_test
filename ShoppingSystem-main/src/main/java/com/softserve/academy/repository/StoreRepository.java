package com.softserve.academy.repository;

import com.softserve.academy.model.Store;
import com.softserve.academy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Store findByName(String name);

    List<Store> findByNameContainingIgnoreCase(String name);

    List<Store> findByLocation(String location);

    List<Store> findByProductsContaining(Product product);
}