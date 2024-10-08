package com.ganzz.web.service;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAllCategories();

}
