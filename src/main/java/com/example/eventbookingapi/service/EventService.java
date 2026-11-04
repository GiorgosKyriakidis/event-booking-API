package com.example.eventbookingapi.service;


import com.example.eventbookingapi.dto.EventRequest;
import com.example.eventbookingapi.dto.EventResponse;
import com.example.eventbookingapi.exception.ResourceNotFoundException;
import com.example.eventbookingapi.model.Event;
import com.example.eventbookingapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;


    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return toResponse(event);
    }
    public EventResponse createEvent(EventRequest request) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setLocation(request.location());
        event.setDateTime(request.dateTime());
        event.setTotalSeats(request.totalSeats());
        event.setAvailableSeats(request.totalSeats()); // server decides this
        return toResponse(eventRepository.save(event));
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    private EventResponse toResponse(Event e) {
        return new EventResponse(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getLocation(),
                e.getDateTime(),
                e.getTotalSeats(),
                e.getAvailableSeats()
        );
    }
}

