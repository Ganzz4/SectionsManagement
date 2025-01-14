package com.ganzz.web.controllers;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.models.Event;
import com.ganzz.web.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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


    @PostMapping("/events/{sectionId}")
    public String createEvent(@PathVariable Long sectionId, @ModelAttribute("event") EventDto eventDto, Model model) {
        eventService.createEvent(sectionId, eventDto);
        return "redirect:/sections/" + sectionId;
    }

}
