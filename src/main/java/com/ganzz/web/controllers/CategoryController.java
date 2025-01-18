package com.ganzz.web.controllers;


import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Category;
import com.ganzz.web.models.Section;
import com.ganzz.web.service.CategoryService;
import com.ganzz.web.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "categories-list";
    }

    @DeleteMapping("/categories/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") long categoryId, Model model) {
        categoryService.delete(categoryId);
        return "redirect:/categories";
    }

    @GetMapping("/categories/new")
    public String createCategoryForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "category-create";
    }

    @PostMapping("/categories/new")
    public String saveCategory(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "category-create";
        }
        categoryService.saveCategory(categoryDto);
        return "redirect:/categories";
    }

    @GetMapping("/categories/{categoryId}/edit")
    public String editCategory(@PathVariable("categoryId") long categoryId, Model model) {
        CategoryDto categoryDto = categoryService.findCategoryById(categoryId);
        model.addAttribute("category", categoryDto);

        return "category-edit";
    }

    @PostMapping("/categories/{categoryId}/edit")
    public String editCategory(@PathVariable("categoryId") Long categoryId, @Valid @ModelAttribute("category") CategoryDto categoryDto,
                               BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "category-edit";
        }

        categoryDto.setId(categoryId);
        categoryService.updateCategory(categoryDto);
        return "redirect:/categories";
    }

}

