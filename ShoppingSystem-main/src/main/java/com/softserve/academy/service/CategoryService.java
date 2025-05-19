package com.softserve.academy.service;

import com.softserve.academy.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category saveCategory(Category category);
    Optional<Category> getCategoryById(Long id);
    Optional<Category> getCategoryByName(String name);
    List<Category> getAllCategories();
    Category updateCategory(Long id, Category categoryDetails);
    void deleteCategory(Long id);
}