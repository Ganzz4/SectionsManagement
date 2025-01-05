package com.ganzz.web.service.impl;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Category;
import com.ganzz.web.models.Section;
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

    @Override
    public Category saveCategory(CategoryDto categoryDto) {
        Category category = mapToCategory(categoryDto);
        return categoryRepository.save(category);

    }

    @Override
    public CategoryDto findCategoryById(long categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        return mapToCategoryDto(category);
    }

    @Override
    public void updateCategory(CategoryDto categoryDto) {
        Category category = mapToCategory(categoryDto);
        categoryRepository.save(category);
    }

    @Override
    public void delete(long sectionId) {
        categoryRepository.deleteById(sectionId);
    }

    private Category mapToCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
        return category;
    }

    private CategoryDto mapToCategoryDto(Category category) {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();

        return categoryDto;
    }
}
