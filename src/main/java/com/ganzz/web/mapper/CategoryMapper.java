package com.ganzz.web.mapper;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.models.Category;

public class CategoryMapper {
    public static Category mapToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
