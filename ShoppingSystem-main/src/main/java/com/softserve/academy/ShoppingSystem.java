package com.softserve.academy;

import lombok.AllArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@AllArgsConstructor

public class ShoppingSystem implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ShoppingSystem.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting application...");

    }
}
