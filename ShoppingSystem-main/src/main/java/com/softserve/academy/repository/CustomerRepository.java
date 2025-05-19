package com.softserve.academy.repository;


import com.softserve.academy.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    //   Optional<Customer> findByEmail(String email);  // You might want to find by email
    //   List<Customer> findByNameContainingIgnoreCase(String name); // Find customers by name


    //   Add other custom methods if needed, for example:
    //   - Finding customers by phone number
    //   - Finding customers who have made purchases within a certain date range
}