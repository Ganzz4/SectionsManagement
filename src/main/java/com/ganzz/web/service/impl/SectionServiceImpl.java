package com.ganzz.web.service.impl;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;
import com.ganzz.web.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ganzz.web.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;
    private final PhotoServiceImpl photoService;


    @Override
    public List<SectionDto> findAllSections() {
        List<Section> sections = sectionRepository.findAll();
        return sections.stream().map((section) -> mapToSectionDto(section)).collect(Collectors.toList());
    }

    @Override
    public Section saveSection(SectionDto sectionDto) {
        sectionDto.setPhotoUrl(photoService.getValidPhotoUrl(sectionDto.getPhotoUrl()));

        Section section = mapToSection(sectionDto);
        return sectionRepository.save(section);
    }

    @Override
    public SectionDto findSectionById(long sectionId) {
        Section section = sectionRepository.findById(sectionId).get();
        return mapToSectionDto(section);
    }

    @Override
    public void updateSection(SectionDto sectionDto) {
        sectionDto.setPhotoUrl(photoService.getValidPhotoUrl(sectionDto.getPhotoUrl()));

        Section section = mapToSection(sectionDto);
        sectionRepository.save(section);
    }

    @Override
    public void delete(long categoryId) {
       sectionRepository.deleteById(categoryId);
    }

    public List<SectionDto> searchSections(String query) {
        List<Section> sections;
        if (query == null || query.trim().isEmpty()) {
            sections = sectionRepository.findAll();
        } else {
            sections = sectionRepository.searchSections(query);
        }
        return sections.stream()
                .map(this::mapToSectionDto)
                .collect(Collectors.toList());
    }


    private Section mapToSection(SectionDto sectionDto) {
        Section section = Section.builder()
                .id(sectionDto.getId())
                .title(sectionDto.getTitle())
                .content(sectionDto.getContent())
                .category(sectionDto.getCategory())
                .contactInfo(sectionDto.getContactInfo())
                .photoUrl(sectionDto.getPhotoUrl())
                .createdOn(sectionDto.getCreatedOn())
                .updatedOn(sectionDto.getUpdatedOn())
                .openingHours(sectionDto.getOpeningHours())
                .location(sectionDto.getLocation())
                .build();
        return section;
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
