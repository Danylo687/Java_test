package com.softserve.academy.service.impl;

import com.softserve.academy.model.Customer;
import com.softserve.academy.repository.CustomerRepository;
import com.softserve.academy.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        // **Important:** You should *never* save the plain-text password directly.
        // In a real application, you would hash it before saving.
        // For demonstration purposes *only*, I'm saving it as is, but this is a HUGE security risk.
        // **DO NOT DO THIS IN PRODUCTION!**
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDetails.getName());
                    existingCustomer.setEmail(customerDetails.getEmail());
                    existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
                    // **Security Warning:** Never directly set the password from user input in a real application!
                    // You MUST hash it before saving.
                    // This is only for demonstration and is extremely insecure.
                    existingCustomer.setPassword(customerDetails.getPassword());
                    return customerRepository.save(existingCustomer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // You can add other methods from CustomerService here
}