package com.ganzz.web.service.impl;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.model.Category;
import com.ganzz.web.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category1 = new Category();
        category1.setId(1L);
        category1.setName("Technology");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Science");

        categoryDto = CategoryDto.builder()
                .id(1L)
                .name("Technology")
                .build();
    }

    @Test
    void testFindAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryDto> result = categoryService.findAllCategories();

        assertEquals(2, result.size());
        assertEquals("Science", result.get(0).getName()); // Sorted alphabetically
        assertEquals("Technology", result.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testSaveCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        categoryService.saveCategory(categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testFindCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));

        CategoryDto result = categoryService.findCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Technology", result.getName());

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoryService.findCategoryById(1L));

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category1);

        categoryService.updateCategory(categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDelete() {
        categoryService.delete(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
