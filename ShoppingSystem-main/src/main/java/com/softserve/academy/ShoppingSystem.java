package com.softserve.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // Ensure JPA repositories are scanned

@SpringBootApplication
// Ensure your repositories are in a sub-package of com.softserve.academy or specify the base package
@EnableJpaRepositories(basePackages = "com.softserve.academy.repository")
public class ShoppingSystem {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingSystem.class, args);
        System.out.println("ShoppingSystem web application started successfully!");
        System.out.println("Access the API endpoints, for example, via http://localhost:8080/api/products");
    }
}
