package com.ganzz.web.mapper;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;

public class SectionMapper {
    public static Section mapToSection(SectionDto sectionDto) {
        return Section.builder()
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
    }

    public static SectionDto mapToSectionDto(Section section) {

        return SectionDto.builder()
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
    }
}
