package com.ganzz.web.mapper;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.model.Event;

public class EventMapper {
    public static Event mapToEvent(EventDto eventDto) {

        if (eventDto == null) {
            return null;
        }
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .type(eventDto.getType())
                .photoUrl(eventDto.getPhotoUrl())
                .createdOn(eventDto.getCreatedOn())
                .updatedOn(eventDto.getUpdatedOn())
                .section(eventDto.getSection())
                .build();
    }

    public static EventDto mapToEventDto(Event event) {
        if (event == null) {
            return null;
        }

        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .type(event.getType())
                .photoUrl(event.getPhotoUrl())
                .createdOn(event.getCreatedOn())
                .updatedOn(event.getUpdatedOn())
                .section(event.getSection())
                .build();
    }
}
