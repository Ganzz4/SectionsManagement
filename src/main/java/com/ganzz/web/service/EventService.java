package com.ganzz.web.service;

import com.ganzz.web.dto.EventDto;
import jakarta.validation.Valid;

import java.util.List;

public interface EventService {
    Long createEvent(Long sectionId, EventDto eventDto);
    List<EventDto> findAllEvents();
    List<EventDto> searchEvents(String query);

    EventDto findByEventId(Long eventId);

    void updateEvent(@Valid EventDto eventDto);

    void delete(long eventId);
}
