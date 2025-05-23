package com.softserve.academy.repository;

import com.softserve.academy.model.Purchase;
import com.softserve.academy.model.Customer;
import com.softserve.academy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByCustomer(Customer customer);

    List<Purchase> findByProduct(Product product);

    List<Purchase> findByCustomerOrderByPurchaseDateDesc(Customer customer);

    List<Purchase> findByCustomerAndProduct(Customer customer, Product product);
}