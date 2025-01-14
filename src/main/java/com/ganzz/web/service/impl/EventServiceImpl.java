package com.ganzz.web.service.impl;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.mapper.EventMapper;
import com.ganzz.web.mapper.SectionMapper;
import com.ganzz.web.models.Event;
import com.ganzz.web.models.Section;
import com.ganzz.web.repository.EventRepository;
import com.ganzz.web.repository.SectionRepository;
import com.ganzz.web.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ganzz.web.mapper.EventMapper.mapToEvent;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final SectionRepository sectionRepository;

    @Override
    public void createEvent(Long sectionId, EventDto eventDto) {
        Section section = sectionRepository.findById(sectionId).get();
        Event event = mapToEvent(eventDto);
        event.setSection(section);
        eventRepository.save(event);
    }

    @Override
    public List<EventDto> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(EventMapper::mapToEventDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDto> searchEvents(String query) {
        List<Event> events;
        if (query == null || query.trim().isEmpty()) {
            events = eventRepository.findAll();
        } else {
            events = eventRepository.searchEvents(query);
        }
        return events.stream()
                .map(EventMapper::mapToEventDto)
                .collect(Collectors.toList());
    }
}
