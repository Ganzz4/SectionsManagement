package com.ganzz.web.service.impl;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.mapper.EventMapper;
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
import static com.ganzz.web.mapper.EventMapper.mapToEventDto;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final SectionRepository sectionRepository;
    private final PhotoServiceImpl photoService;

    @Override
    public Long createEvent(Long sectionId, EventDto eventDto) {
        Section section = sectionRepository.findById(sectionId).orElseThrow(() -> new RuntimeException("Section not found"));
        eventDto.setPhotoUrl(photoService.getValidPhotoUrl(eventDto.getPhotoUrl()));
        Event event = mapToEvent(eventDto);
        event.setSection(section);
        Event savedEvent = eventRepository.save(event);
        return savedEvent.getId();
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

    @Override
    public EventDto findByEventId(Long eventId) {
        return mapToEventDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found")));
    }

    @Override
    public void updateEvent(EventDto eventDto) {
        eventDto.setPhotoUrl(photoService.getValidPhotoUrl(eventDto.getPhotoUrl()));
        eventRepository.save(mapToEvent(eventDto));
    }

    @Override
    public void delete(long eventId) {
        eventRepository.deleteById(eventId);
    }
}
