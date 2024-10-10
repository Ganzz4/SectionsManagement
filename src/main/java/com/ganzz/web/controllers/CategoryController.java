package com.ganzz.web.controllers;


import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Category;
import com.ganzz.web.models.Section;
import com.ganzz.web.service.CategoryService;
import com.ganzz.web.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "categories-list";
    }


    @GetMapping("/categories/new")
    public String createCategoryForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "category-create";
    }

    @PostMapping("/categories/new")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/{categoryId}/edit")
    public String editCategory(@PathVariable("categoryId") long categoryId, Model model) {
        CategoryDto categoryDto = categoryService.findCategoryById(categoryId);
        model.addAttribute("category", categoryDto);

        return "category-edit";
    }

    @PostMapping("/categories/{categoryId}/edit")
    public String editCategory(@PathVariable("categoryId") Long categoryId, @ModelAttribute("category") CategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        categoryService.updateCategory(categoryDto);
        return "redirect:/categories";
    }

}

