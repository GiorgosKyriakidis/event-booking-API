package com.example.eventbookingapi.controller;

import com.example.eventbookingapi.model.Event;
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
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // Endpoint για να δημιουργήσουμε ένα νέο event (POST Request)
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "Event with ID " + id + " was deleted successfully!";
    }
}
