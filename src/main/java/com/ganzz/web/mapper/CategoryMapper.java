package com.ganzz.web.mapper;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.model.Category;

public class CategoryMapper {
    public static Category mapToCategory(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
