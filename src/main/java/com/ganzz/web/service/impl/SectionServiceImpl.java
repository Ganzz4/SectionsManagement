package com.ganzz.web.service.impl;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;
import com.ganzz.web.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.SectionRepository;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Service
public class SectionServiceImpl implements SectionService {
    private SectionRepository sectionRepository;

    @Autowired
    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<SectionDto> findAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream().map((section) -> mapToSectionDto(section)).collect(Collectors.toList());
    }

    private SectionDto mapToSectionDto(Section section) {
        SectionDto sectionDto = SectionDto.builder()
                .id(section.getId())
                .title(section.getTitle())
                .content(section.getContent())
                .category(section.getCategory())
                .contactInfo(section.getContactInfo())
                .photoUrl(section.getPhotoUrl())
                .createdOn(section.getCreatedOn())
                .updatedOn(section.getUpdatedOn())
                .openingHours(section.getOpeningHours())
                .location(section.getLocation())
                .build();

        return sectionDto;
    }
}
