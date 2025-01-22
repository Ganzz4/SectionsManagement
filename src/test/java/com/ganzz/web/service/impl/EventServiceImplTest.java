package com.ganzz.web.service.impl;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.model.Event;
import com.ganzz.web.model.Section;
import com.ganzz.web.repository.EventRepository;
import com.ganzz.web.repository.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private PhotoServiceImpl photoService;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventDto eventDto;

    @BeforeEach
    void setUp() {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            Section section = new Section();
            section.setId(1L);

            event = new Event();
            event.setId(1L);
            event.setSection(section);

            eventDto = new EventDto();
            eventDto.setPhotoUrl("photo_url");
            eventDto.setId(1L);
            eventDto.setSection(section);

            when(sectionRepository.findById(1L)).thenReturn(Optional.of(section));
            when(photoService.getValidPhotoUrl(anyString())).thenReturn("valid_photo_url");
            when(eventRepository.save(any(Event.class))).thenReturn(event);
            when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
            when(eventRepository.findAll()).thenReturn(Collections.singletonList(event));
            when(eventRepository.searchEvents(anyString())).thenReturn(Collections.singletonList(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateEvent() {
        Long eventId = eventService.createEvent(1L, eventDto);
        assertNotNull(eventId);
        assertEquals(1L, eventId);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testFindAllEvents() {
        List<EventDto> events = eventService.findAllEvents();
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event.getId(), events.get(0).getId());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testSearchEventsWithValidQuery() {
        List<EventDto> events = eventService.searchEvents("query");
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event.getId(), events.get(0).getId());
        verify(eventRepository, times(1)).searchEvents("query");
    }

    @Test
    void testSearchEventsWithEmptyQuery() {
        var events = eventService.searchEvents("");
        assertNotNull(events);
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testFindByEventId() {
        EventDto foundEvent = eventService.findByEventId(1L);
        assertNotNull(foundEvent);
        assertEquals(event.getId(), foundEvent.getId());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEvent() {
        eventService.updateEvent(eventDto);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testDeleteEvent() {
        eventService.delete(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateEventWhenSectionNotFound() {
        eventDto.setSection(null);
        when(sectionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> eventService.createEvent(1L, eventDto));
    }
}
