package com.ganzz.web.controller;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.model.Event;
import com.ganzz.web.model.Section;
import com.ganzz.web.model.UserEntity;
import com.ganzz.web.security.SecurityUtil;
import com.ganzz.web.service.EventService;
import com.ganzz.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private EventController eventController;

    private EventDto eventDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        Section section = new Section();
        section.setId(1L);

        eventDto = EventDto.builder()
                .id(1L)
                .name("Test Event")
                .type("Conference")
                .startTime(now.plusDays(1))
                .endTime(now.plusDays(2))
                .photoUrl("/images/test.jpg")
                .createdOn(now)
                .updatedOn(now)
                .section(section)
                .build();

        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
    }

    @Test
    void createEventForm_ShouldReturnCreateView() {
        Long sectionId = 1L;

        String viewName = eventController.createEventForm(sectionId, model);

        verify(model).addAttribute(eq("sectionId"), eq(sectionId));
        verify(model).addAttribute(eq("event"), any(Event.class));
        assertEquals("event-create", viewName);
    }

    @Test
    void editEventForm_ShouldReturnEditView() {
        long eventId = 1L;
        when(eventService.findByEventId(eventId)).thenReturn(eventDto);

        String viewName = eventController.editEventForm(eventId, model);

        verify(model).addAttribute("event", eventDto);
        verify(model).addAttribute(eq("defaultPhotoPath"), any());
        assertEquals("event-edit", viewName);
    }

    @Test
    void eventList_ShouldReturnAllEvents() {
        List<EventDto> events = Collections.singletonList(eventDto);
        when(eventService.findAllEvents()).thenReturn(events);

        String viewName = eventController.eventList(model);

        verify(model).addAttribute("events", events);
        assertEquals("events-list", viewName);
    }

    @Test
    void searchEvent_ShouldReturnMatchingEvents() {
        String query = "test";
        List<EventDto> events = Collections.singletonList(eventDto);
        when(eventService.searchEvents(query)).thenReturn(events);

        String viewName = eventController.searchEvent(query, model);

        verify(model).addAttribute("events", events);
        assertEquals("events-list", viewName);
    }

    @Test
    void viewEvent_WithAuthenticatedUser_ShouldReturnEventDetails() {
        try (MockedStatic<SecurityUtil> securityUtil = mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getSessionUser).thenReturn("testUser");
            when(userService.findByUsername("testUser")).thenReturn(userEntity);
            when(eventService.findByEventId(1L)).thenReturn(eventDto);

            String viewName = eventController.viewEvent(1L, model);

            verify(model).addAttribute("event", eventDto);
            verify(model).addAttribute("user", userEntity);
            verify(model, times(3)).addAttribute(matches("formatted.*"), anyString());
            assertEquals("event-detail", viewName);
        }
    }

    @Test
    void viewEvent_WithoutAuthenticatedUser_ShouldReturnEventDetails() {
        try (MockedStatic<SecurityUtil> securityUtil = mockStatic(SecurityUtil.class)) {
            securityUtil.when(SecurityUtil::getSessionUser).thenReturn(null);
            when(eventService.findByEventId(1L)).thenReturn(eventDto);

            String viewName = eventController.viewEvent(1L, model);

            verify(model).addAttribute("event", eventDto);
            verify(model).addAttribute(eq("user"), any(UserEntity.class));
            verify(model, times(3)).addAttribute(matches("formatted.*"), anyString());
            assertEquals("event-detail", viewName);
        }
    }

    @Test
    void updateEvent_WithValidData_ShouldRedirectToEventsList() {
        when(bindingResult.hasErrors()).thenReturn(false);

        EventDto existingEvent = EventDto.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .type(eventDto.getType())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .photoUrl(eventDto.getPhotoUrl())
                .createdOn(eventDto.getCreatedOn())
                .updatedOn(eventDto.getUpdatedOn())
                .section(eventDto.getSection())
                .build();

        when(eventService.findByEventId(1L)).thenReturn(existingEvent);

        String viewName = eventController.updateEvent(1L, eventDto, bindingResult, model);

        verify(eventService).updateEvent(eventDto);
        assertEquals("redirect:/events", viewName);
    }

    @Test
    void updateEvent_WithInvalidData_ShouldReturnToEditForm() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = eventController.updateEvent(1L, eventDto, bindingResult, model);

        verify(model).addAttribute("event", eventDto);
        assertEquals("event-edit", viewName);
    }

    @Test
    void createEvent_WithValidData_ShouldRedirectToEventDetail() {
        Long sectionId = 1L;
        Long eventId = 1L;
        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.createEvent(eq(sectionId), any(EventDto.class))).thenReturn(eventId);

        String viewName = eventController.createEvent(sectionId, eventDto, bindingResult, model);

        assertEquals("redirect:/events/" + eventId, viewName);
    }

    @Test
    void createEvent_WithInvalidData_ShouldReturnToEditForm() {
        Long sectionId = 1L;
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = eventController.createEvent(sectionId, eventDto, bindingResult, model);

        verify(model).addAttribute("event", eventDto);
        assertEquals("event-edit", viewName);
    }

    @Test
    void deleteEvent_ShouldRedirectToEventsList() {
        String viewName = eventController.deleteEvent(1L, model);

        verify(eventService).delete(1L);
        assertEquals("redirect:/events", viewName);
    }
}