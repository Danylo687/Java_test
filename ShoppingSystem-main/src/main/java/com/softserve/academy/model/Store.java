package com.softserve.academy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Store name cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Location cannot be blank")
    private String location;

    @Pattern(regexp = "\\+?[0-9\\-\\s]{7,20}", message = "Invalid contact number format")
    private String contactNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Builder.Default
    @ManyToMany(mappedBy = "stores", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();
}