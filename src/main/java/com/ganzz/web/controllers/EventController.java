package com.ganzz.web.controllers;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.models.Event;
import com.ganzz.web.models.UserEntity;
import com.ganzz.web.security.SecurityUtil;
import com.ganzz.web.service.EventService;
import com.ganzz.web.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EventController {
    @Value("${app.default-photo:/images/default-section.jpg}")
    private String defaultEventPhoto;

    private final EventService eventService;
    private final UserService userService;



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/events/{sectionId}/new")
    public String createEventForm(@PathVariable Long sectionId, Model model) {
        Event event = new Event();
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("event", event);
        return "event-create";
    }

    @PreAuthorize("@securityExpression.canModifyEvent(#eventId)")
    @GetMapping("/events/{eventId}/edit")
    public String editEventForm(@PathVariable("eventId") long eventId, Model model) {
        EventDto eventDto = eventService.findByEventId(eventId);
        model.addAttribute("event", eventDto);
        model.addAttribute("defaultPhotoPath", defaultEventPhoto);

        return "event-edit";
    }


    @GetMapping("/events")
    public String eventList(Model model) {
        List<EventDto> events = eventService.findAllEvents();
        model.addAttribute("events", events);
        return "events-list";
    }


    @GetMapping("/events/search")
    public String searchEvent(@RequestParam(value = "query") String query, Model model) {
        List<EventDto> events = eventService.searchEvents(query);
        model.addAttribute("events", events);

        return "events-list";
    }

    @GetMapping("/events/{eventId}")
    public String viewEvent(@PathVariable(value = "eventId") Long eventId, Model model) {
        EventDto eventDto = eventService.findByEventId(eventId);


        UserEntity user = new UserEntity();
        String username = SecurityUtil.getSessionUser();
        if (username != null) {
            user = userService.findByUsername(username);
        }
        model.addAttribute("user", user);

        model.addAttribute("event", eventDto);
        model.addAttribute("formattedCreatedOn", formatDateTime(eventDto.getCreatedOn()));
        model.addAttribute("formattedStartTime", formatDateTime(eventDto.getStartTime()));
        model.addAttribute("formattedEndTime", formatDateTime(eventDto.getEndTime()));

        return "event-detail";
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(dt -> dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .orElse("");
    }

    @PreAuthorize("@securityExpression.canModifyEvent(#eventId)")
    @PostMapping("/events/{eventId}/edit")
    public String updateEvent(@PathVariable("eventId") Long eventId, @Valid @ModelAttribute("event") EventDto eventDto
            , BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("event", eventDto);
            return "event-edit";
        }

        EventDto existingEvent = eventService.findByEventId(eventId);
        eventDto.setSection(existingEvent.getSection());

        eventDto.setId(eventId);
        eventService.updateEvent(eventDto);
        return "redirect:/events";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/events/{sectionId}")
    public String createEvent(@PathVariable Long sectionId, @ModelAttribute("event") EventDto eventDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("event", eventDto);
            return "event-edit";
        }
        eventService.createEvent(sectionId, eventDto);
        Long eventId = eventService.createEvent(sectionId, eventDto);
        return "redirect:/events/" + eventId;
    }

    @PreAuthorize("@securityExpression.canModifyEvent(#eventId)")
    @DeleteMapping("/events/{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") long eventId, Model model) {
        eventService.delete(eventId);
        return "redirect:/events";
    }
}