package com.softserve.academy.service;


import com.softserve.academy.model.Customer;
import java.util.List;
import java.util.Optional;


public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Optional<Customer> getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer customerDetails);
    void deleteCustomer(Long id);


    //   Optional<Customer> getCustomerByEmail(String email);  // If you add this to the repository
}