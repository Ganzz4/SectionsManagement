package com.ganzz.web.controller;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.model.Category;
import com.ganzz.web.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDto createSampleCategoryDto() {
        return CategoryDto.builder()
                .id(1L)
                .name("Test Category")
                .build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listCategories_ShouldReturnAllCategoriesAndCorrectView() {
        CategoryDto category1 = createSampleCategoryDto();
        CategoryDto category2 = CategoryDto.builder()
                .id(2L)
                .name("Category 2")
                .build();
        List<CategoryDto> categories = Arrays.asList(category1, category2);
        when(categoryService.findAllCategories()).thenReturn(categories);

        String viewName = categoryController.listCategories(model);

        verify(model).addAttribute("categories", categories);
        assertEquals("categories-list", viewName);
    }

    @Test
    void deleteCategory_ShouldDeleteAndRedirect() {
        CategoryDto categoryDto = createSampleCategoryDto();

        String viewName = categoryController.deleteCategory(categoryDto.getId(), model);

        verify(categoryService).delete(categoryDto.getId());
        assertEquals("redirect:/categories", viewName);
    }

    @Test
    void createCategoryForm_ShouldReturnCreateFormView() {
        String viewName = categoryController.createCategoryForm(model);

        verify(model).addAttribute(eq("category"), any(Category.class));
        assertEquals("category-create", viewName);
    }

    @Test
    void saveCategory_WhenValidationErrors_ShouldReturnCreateForm() {
        CategoryDto categoryDto = createSampleCategoryDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = categoryController.saveCategory(categoryDto, bindingResult, model);

        assertEquals("category-create", viewName);
        verify(categoryService, never()).saveCategory(any(CategoryDto.class));
    }

    @Test
    void saveCategory_WhenSuccessful_ShouldSaveAndRedirect() {
        CategoryDto categoryDto = createSampleCategoryDto();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = categoryController.saveCategory(categoryDto, bindingResult, model);

        verify(categoryService).saveCategory(categoryDto);
        assertEquals("redirect:/categories", viewName);
    }

    @Test
    void editCategory_ShouldReturnEditFormWithCategory() {
        CategoryDto categoryDto = createSampleCategoryDto();
        when(categoryService.findCategoryById(categoryDto.getId())).thenReturn(categoryDto);

        String viewName = categoryController.editCategory(categoryDto.getId(), model);

        verify(model).addAttribute("category", categoryDto);
        assertEquals("category-edit", viewName);
    }

    @Test
    void editCategory_Post_WhenValidationErrors_ShouldReturnEditForm() {
        CategoryDto categoryDto = createSampleCategoryDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = categoryController.editCategory(categoryDto.getId(), categoryDto, bindingResult);

        assertEquals("category-edit", viewName);
        verify(categoryService, never()).updateCategory(any(CategoryDto.class));
    }

    @Test
    void editCategory_Post_WhenSuccessful_ShouldUpdateAndRedirect() {
        Long categoryId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Test Category")
                .build();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = categoryController.editCategory(categoryId, categoryDto, bindingResult);

        assertEquals(categoryId, categoryDto.getId());
        verify(categoryService).updateCategory(categoryDto);
        assertEquals("redirect:/categories", viewName);
    }
}