package com.ganzz.web.controllers;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Category;
import com.ganzz.web.models.Section;
import com.ganzz.web.service.CategoryService;
import com.ganzz.web.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SectionController {
    private SectionService sectionService;
    private CategoryService categoryService;

    @Autowired
    public SectionController(SectionService sectionService, CategoryService categoryService) {
        this.sectionService = sectionService;
        this.categoryService = categoryService;
    }
    @GetMapping("/sections")
    public String listSections(Model model) {
        List<SectionDto> sections = sectionService.findAllSections();
        model.addAttribute("sections", sections);
        return "sections-list";
    }

    @GetMapping("/sections/{sectionId}")
public String sectionDetail(@PathVariable("sectionId") long sectionId, Model model) {
SectionDto sectionDto = sectionService.findSectionById(sectionId);
model.addAttribute("section", sectionDto);
return "section-detail";
    }
    @GetMapping("/sections/new")
    public String createSectionForm(Model model) {
        Section section = new Section();
        model.addAttribute("section", section);

        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);

        return "section-create";
    }

    @PostMapping("/sections/new")
    public String saveSection(@Valid @ModelAttribute("section") SectionDto sectionDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<CategoryDto> categories = categoryService.findAllCategories();
            model.addAttribute("categories", categories);
            return "section-create";
        }
        sectionService.saveSection(sectionDto);
        return "redirect:/sections";
    }

    @GetMapping("/sections/{sectionId}/edit")
    public String editSection(@PathVariable("sectionId") long sectionId, Model model) {
        SectionDto sectionDto = sectionService.findSectionById(sectionId);
        model.addAttribute("section", sectionDto);

        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);

        return "section-edit";
    }

    @PostMapping("/sections/{sectionId}/edit")
    public String editSection(@PathVariable("sectionId") Long sectionId, @Valid @ModelAttribute("section") SectionDto sectionDto
    , BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            List<CategoryDto> categories = categoryService.findAllCategories();
            model.addAttribute("categories", categories);
            return "section-edit";
        }
        sectionDto.setId(sectionId);
        sectionService.updateSection(sectionDto);
        return "redirect:/sections";
    }

}
