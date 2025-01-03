package com.ganzz.web.service;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAllCategories();

    Category saveCategory(CategoryDto categoryDto);

    CategoryDto findCategoryById(long categoryId);

    void updateCategory(CategoryDto categoryDto);
}
