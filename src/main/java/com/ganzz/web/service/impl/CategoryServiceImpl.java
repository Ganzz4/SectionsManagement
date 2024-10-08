package com.ganzz.web.service.impl;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.models.Category;
import com.ganzz.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ganzz.web.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        List<Category> sections = categoryRepository.findAll();
        return sections.stream().map((section) -> mapToCategoryDto(section)).collect(Collectors.toList());
    }

    private CategoryDto mapToCategoryDto(Category category) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

        return categoryDto;
    }
}
