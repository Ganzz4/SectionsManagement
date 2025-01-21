package com.ganzz.web.service;

import com.ganzz.web.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAllCategories();

    void saveCategory(CategoryDto categoryDto);

    CategoryDto findCategoryById(long categoryId);

    void updateCategory(CategoryDto categoryDto);

    void delete(long categoryId);
}
