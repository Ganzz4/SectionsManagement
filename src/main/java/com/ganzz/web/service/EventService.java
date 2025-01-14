package com.ganzz.web.service;

import com.ganzz.web.dto.EventDto;

import java.util.List;

public interface EventService {
    void createEvent(Long sectionId, EventDto eventDto);
    List<EventDto> findAllEvents();
}
