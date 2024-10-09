package com.ganzz.web.controllers;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SectionController {
    private SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/sections")
    public String sections(Model model) {
        List<SectionDto> sections = sectionService.findAllSections();
        model.addAttribute("sections", sections);
        return "sections-list";
    }
}
