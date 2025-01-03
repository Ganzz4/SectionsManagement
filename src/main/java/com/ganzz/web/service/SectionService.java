package com.ganzz.web.service;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;

import java.util.List;


public interface SectionService {
    List<SectionDto> findAllSections();
    Section saveSection(SectionDto sectionDto);
    SectionDto findSectionById(long sectionId);

    void updateSection(SectionDto sectionDto);
}
