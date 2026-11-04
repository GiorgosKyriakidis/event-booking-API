package com.example.eventbookingapi.controller;

import com.example.eventbookingapi.dto.EventRequest;
import com.example.eventbookingapi.dto.EventResponse;
import com.example.eventbookingapi.service.EventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import jakarta.validation.Valid;


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
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse created = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
