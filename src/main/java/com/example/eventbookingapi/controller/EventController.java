package com.example.eventbookingapi.controller;

import com.example.eventbookingapi.dto.EventRequest;
import com.example.eventbookingapi.dto.EventResponse;
import com.example.eventbookingapi.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    // Συνδέουμε τον Controller με το Service
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Endpoint για να πάρουμε όλα τα events (GET Request)
    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }


    @PostMapping
    public EventResponse createEvent(@RequestBody EventRequest request) {
        return eventService.createEvent(request);
    }
    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "Event with ID " + id + " was deleted successfully!";
    }
}
