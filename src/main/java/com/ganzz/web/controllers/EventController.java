package com.ganzz.web.controllers;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.models.Event;
import com.ganzz.web.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/events/{sectionId}/new")
    public String createEventForm(@PathVariable Long sectionId, Model model) {
        Event event = new Event();
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("event", event);
        return "event-create";
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


    @PostMapping("/events/{sectionId}")
    public String createEvent(@PathVariable Long sectionId, @ModelAttribute("event") EventDto eventDto, Model model) {
        eventService.createEvent(sectionId, eventDto);
        return "redirect:/sections/" + sectionId;
    }



}
