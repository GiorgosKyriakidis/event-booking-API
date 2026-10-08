package com.example.eventbookingapi.service;


import com.example.eventbookingapi.model.Event;
import com.example.eventbookingapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    // Εδώ κάνουμε "Dependency Injection".
    // Λέμε στο Spring να δώσει αυτόματα το Repository στο Service.
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // 1. Μέθοδος για να φέρουμε όλα τα events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 2. Μέθοδος για να αποθηκεύσουμε ένα νέο event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
    // 3. Βρες ένα event με βάση το ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    // 4. Διαγραφή ενός event με βάση το ID
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}

