package com.ganzz.web.controllers;

import com.ganzz.web.dto.CategoryDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;
import com.ganzz.web.models.UserEntity;
import com.ganzz.web.security.SecurityUtil;
import com.ganzz.web.service.CategoryService;
import com.ganzz.web.service.SectionService;
import com.ganzz.web.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SectionController {
    @Value("${app.default-photo:/images/default-section.jpg}")
    private String defaultPhoto;

    private final SectionService sectionService;
    private final CategoryService categoryService;
    private final UserService userService;


    private void addCategoriesToModel(Model model) {
        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        String formattedDate;
        if (sectionDto.getCreatedOn() != null) {
            formattedDate = sectionDto.getCreatedOn().format(formatter);
        }else{
            formattedDate = "";
        }

        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.findByUsername(username);
        }
        model.addAttribute("user", user);

        model.addAttribute("formattedDate", formattedDate);
        model.addAttribute("section", sectionDto);
        return "section-detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sections/new")
    public String createSectionForm(Model model) {
        Section section = new Section();
        model.addAttribute("section", section);

        addCategoriesToModel(model);
        return "section-create";
    }

    @PreAuthorize("@securityExpression.canModifySection(#sectionId)")
    @DeleteMapping("/sections/{sectionId}/delete")
    public String deleteSection(@PathVariable("sectionId") long sectionId, Model model) {
        sectionService.delete(sectionId);
        return "redirect:/sections";
    }

    @GetMapping("/sections/search")
    public String searchSection(@RequestParam(value = "query") String query, Model model) {
        List<SectionDto> sections = sectionService.searchSections(query);
        model.addAttribute("sections", sections);

        return "sections-list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/sections/new")
    public String saveSection(@Valid @ModelAttribute("section") SectionDto sectionDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addCategoriesToModel(model);
            return "section-create";
        }
        sectionService.saveSection(sectionDto);
        return "redirect:/sections";
    }

    @PreAuthorize("@securityExpression.canModifySection(#sectionId)")
    @GetMapping("/sections/{sectionId}/edit")
    public String editSectionForm(@PathVariable("sectionId") long sectionId, Model model) {
        SectionDto sectionDto = sectionService.findSectionById(sectionId);
        model.addAttribute("section", sectionDto);
        model.addAttribute("defaultPhotoPath", defaultPhoto);
        addCategoriesToModel(model);

        return "section-edit";
    }

    @PreAuthorize("@securityExpression.canModifySection(#sectionId)")
    @PostMapping("/sections/{sectionId}/edit")
    public String editSection(@PathVariable("sectionId") Long sectionId, @Valid @ModelAttribute("section") SectionDto sectionDto
            , BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            addCategoriesToModel(model);
            model.addAttribute("section", sectionDto);
            return "section-edit";
        }
        sectionDto.setId(sectionId);
        sectionService.updateSection(sectionDto);
        return "redirect:/sections";
    }

}
