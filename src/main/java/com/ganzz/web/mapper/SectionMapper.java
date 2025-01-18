package com.ganzz.web.mapper;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Section;

import java.util.stream.Collectors;

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
                .createdBy(sectionDto.getCreatedBy())
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
                .createdBy(section.getCreatedBy())
                .events(section.getEvents().stream().map((EventMapper::mapToEventDto)).collect(Collectors.toList()))
                .build();
    }
}
