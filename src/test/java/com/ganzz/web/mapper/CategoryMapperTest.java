package com.ganzz.web.mapper;


import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    @Test
    void mapToCategory_ShouldMapCategoryDtoToCategory() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name("Test Category")
                .build();

        Category category = CategoryMapper.mapToCategory(categoryDto);

        assertNotNull(category, "The mapped Category should not be null");
        assertEquals(categoryDto.getId(), category.getId(), "The IDs should match");
        assertEquals(categoryDto.getName(), category.getName(), "The names should match");
    }

    @Test
    void mapToCategoryDto_ShouldMapCategoryToCategoryDto() {
        Category category = Category.builder()
                .id(1L)
                .name("Test Category")
                .build();

        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);

        assertNotNull(categoryDto, "The mapped CategoryDto should not be null");
        assertEquals(category.getId(), categoryDto.getId(), "The IDs should match");
        assertEquals(category.getName(), categoryDto.getName(), "The names should match");
    }

    @Test
    void mapToCategory_NullInput_ShouldReturnNull() {
        Category category = CategoryMapper.mapToCategory(null);

        assertNull(category, "Mapping a null CategoryDto should return null");
    }

    @Test
    void mapToCategoryDto_NullInput_ShouldReturnNull() {
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(null);

        assertNull(categoryDto, "Mapping a null Category should return null");
    }
}

