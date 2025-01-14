package com.ganzz.web.service;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.dto.SectionDto;
import jakarta.validation.Valid;

import java.util.List;

public interface EventService {
    void createEvent(Long sectionId, EventDto eventDto);
    List<EventDto> findAllEvents();
    List<EventDto> searchEvents(String query);

    EventDto findByEventId(Long eventId);

    void updateEvent(@Valid EventDto eventDto);

    void delete(long eventId);
}
