package com.ganzz.web.mapper;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.model.Event;
import com.ganzz.web.model.Section;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    @Test
    void mapToEvent_ShouldMapEventDtoToEvent() {
        Section section = new Section();
        section.setId(1L);

        EventDto eventDto = EventDto.builder()
                .id(1L)
                .name("Test Event")
                .startTime(LocalDateTime.of(2025, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2025, 1, 1, 12, 0))
                .type("Online")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.of(2025, 1, 1, 9, 0))
                .updatedOn(LocalDateTime.of(2025, 1, 1, 11, 0))
                .section(section)
                .build();

        Event event = EventMapper.mapToEvent(eventDto);

        assertNotNull(event, "The mapped Event should not be null");
        assertEquals(eventDto.getId(), event.getId(), "The IDs should match");
        assertEquals(eventDto.getName(), event.getName(), "The names should match");
        assertEquals(eventDto.getStartTime(), event.getStartTime(), "The start times should match");
        assertEquals(eventDto.getEndTime(), event.getEndTime(), "The end times should match");
        assertEquals(eventDto.getType(), event.getType(), "The types should match");
        assertEquals(eventDto.getPhotoUrl(), event.getPhotoUrl(), "The photo URLs should match");
        assertEquals(eventDto.getCreatedOn(), event.getCreatedOn(), "The createdOn timestamps should match");
        assertEquals(eventDto.getUpdatedOn(), event.getUpdatedOn(), "The updatedOn timestamps should match");
        assertEquals(eventDto.getSection(), event.getSection(), "The sections should match");
    }

    @Test
    void mapToEventDto_ShouldMapEventToEventDto() {
        Section section = new Section();
        section.setId(1L);

        Event event = Event.builder()
                .id(1L)
                .name("Test Event")
                .startTime(LocalDateTime.of(2025, 1, 1, 10, 0))
                .endTime(LocalDateTime.of(2025, 1, 1, 12, 0))
                .type("Online")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.of(2025, 1, 1, 9, 0))
                .updatedOn(LocalDateTime.of(2025, 1, 1, 11, 0))
                .section(section)
                .build();

        EventDto eventDto = EventMapper.mapToEventDto(event);

        assertNotNull(eventDto, "The mapped EventDto should not be null");
        assertEquals(event.getId(), eventDto.getId(), "The IDs should match");
        assertEquals(event.getName(), eventDto.getName(), "The names should match");
        assertEquals(event.getStartTime(), eventDto.getStartTime(), "The start times should match");
        assertEquals(event.getEndTime(), eventDto.getEndTime(), "The end times should match");
        assertEquals(event.getType(), eventDto.getType(), "The types should match");
        assertEquals(event.getPhotoUrl(), eventDto.getPhotoUrl(), "The photo URLs should match");
        assertEquals(event.getCreatedOn(), eventDto.getCreatedOn(), "The createdOn timestamps should match");
        assertEquals(event.getUpdatedOn(), eventDto.getUpdatedOn(), "The updatedOn timestamps should match");
        assertEquals(event.getSection(), eventDto.getSection(), "The sections should match");
    }

    @Test
    void mapToCategory_NullInput_ShouldReturnNull() {
        Event event = EventMapper.mapToEvent(null);

        assertNull(event, "Mapping a null CategoryDto should return null");
    }

    @Test
    void mapToCategoryDto_NullInput_ShouldReturnNull() {
        EventDto eventDto = EventMapper.mapToEventDto(null);

        assertNull(eventDto, "Mapping a null Category should return null");
    }

}

