package com.ganzz.web.service;

import com.ganzz.web.dto.EventDto;

public interface EventService {
    void createEvent(Long sectionId, EventDto eventDto);
}
