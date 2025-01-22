package com.ganzz.web.service.impl;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.mapper.CategoryMapper;
import com.ganzz.web.model.Category;
import com.ganzz.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ganzz.web.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static com.ganzz.web.mapper.CategoryMapper.mapToCategory;
import static com.ganzz.web.mapper.CategoryMapper.mapToCategoryDto;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        categories.sort((category1, category2) -> category1.getName().compareToIgnoreCase(category2.getName()));

        return categories.stream().map(CategoryMapper::mapToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public void saveCategory(CategoryDto categoryDto) {
        Category category = mapToCategory(categoryDto);
        categoryRepository.save(category);

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


}
