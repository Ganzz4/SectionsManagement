package com.ganzz.web.mapper;

import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.model.Category;
import com.ganzz.web.model.Section;
import com.ganzz.web.model.Event;
import com.ganzz.web.dto.EventDto;
import com.ganzz.web.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SectionMapperTest {

    @Test
    void mapToSection_ShouldMapSectionDtoToSection() {
        SectionDto sectionDto = SectionDto.builder()
                .id(1L)
                .title("Test Section")
                .content("This is a test section.")
                .category(new Category(1L,"Test Category"))
                .contactInfo("test@example.com")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .openingHours("9:00 AM - 5:00 PM")
                .location("Test Location")
                .createdBy(new UserEntity())
                .events(Collections.emptyList())
                .build();

        Section section = SectionMapper.mapToSection(sectionDto);

        assertNotNull(section, "The mapped Section should not be null");
        assertEquals(sectionDto.getId(), section.getId(), "The IDs should match");
        assertEquals(sectionDto.getTitle(), section.getTitle(), "The titles should match");
        assertEquals(sectionDto.getContent(), section.getContent(), "The contents should match");
        assertEquals(sectionDto.getCategory(), section.getCategory(), "The categories should match");
        assertEquals(sectionDto.getContactInfo(), section.getContactInfo(), "The contact information should match");
        assertEquals(sectionDto.getPhotoUrl(), section.getPhotoUrl(), "The photo URLs should match");
        assertEquals(sectionDto.getCreatedOn(), section.getCreatedOn(), "The createdOn timestamps should match");
        assertEquals(sectionDto.getUpdatedOn(), section.getUpdatedOn(), "The updatedOn timestamps should match");
        assertEquals(sectionDto.getOpeningHours(), section.getOpeningHours(), "The opening hours should match");
        assertEquals(sectionDto.getLocation(), section.getLocation(), "The locations should match");
        assertEquals(sectionDto.getCreatedBy(), section.getCreatedBy(), "The createdBy fields should match");
    }

    @Test
    void mapToSectionDto_ShouldMapSectionToSectionDto() {
        Event event = Event.builder().id(1L).name("Event 1").build();
        EventDto eventDto = EventDto.builder().id(1L).name("Event 1").build();

        Section section = Section.builder()
                .id(1L)
                .title("Test Section")
                .content("This is a test section.")
                .category(new Category(1L, "Test Category"))
                .contactInfo("test@example.com")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .openingHours("9:00 AM - 5:00 PM")
                .location("Test Location")
                .createdBy(new UserEntity())
                .events(List.of(event))
                .build();

        try (MockedStatic<EventMapper> mockedEventMapper = mockStatic(EventMapper.class)) {
            mockedEventMapper.when(() -> EventMapper.mapToEventDto(event)).thenReturn(eventDto);

            SectionDto sectionDto = SectionMapper.mapToSectionDto(section);

            assertNotNull(sectionDto, "The mapped SectionDto should not be null");
            assertEquals(section.getId(), sectionDto.getId(), "The IDs should match");
            assertEquals(section.getTitle(), sectionDto.getTitle(), "The titles should match");
            assertEquals(section.getContent(), sectionDto.getContent(), "The contents should match");
            assertEquals(section.getCategory(), sectionDto.getCategory(), "The categories should match");
            assertEquals(section.getContactInfo(), sectionDto.getContactInfo(), "The contact information should match");
            assertEquals(section.getPhotoUrl(), sectionDto.getPhotoUrl(), "The photo URLs should match");
            assertEquals(section.getCreatedOn(), sectionDto.getCreatedOn(), "The createdOn timestamps should match");
            assertEquals(section.getUpdatedOn(), sectionDto.getUpdatedOn(), "The updatedOn timestamps should match");
            assertEquals(section.getOpeningHours(), sectionDto.getOpeningHours(), "The opening hours should match");
            assertEquals(section.getLocation(), sectionDto.getLocation(), "The locations should match");
            assertEquals(section.getCreatedBy(), sectionDto.getCreatedBy(), "The createdBy fields should match");
            assertEquals(1, sectionDto.getEvents().size(), "The number of events should match");
            assertEquals(eventDto.getId(), sectionDto.getEvents().get(0).getId(), "The event IDs should match");
        }
    }

    @Test
    void mapToSection_NullInput_ShouldReturnNull() {
        Section section = SectionMapper.mapToSection(null);

        assertNull(section, "Mapping a null SectionDto should return null");
    }

    @Test
    void mapToSectionDto_NullInput_ShouldReturnNull() {
        SectionDto sectionDto = SectionMapper.mapToSectionDto(null);

        assertNull(sectionDto, "Mapping a null Section should return null");
    }
}
