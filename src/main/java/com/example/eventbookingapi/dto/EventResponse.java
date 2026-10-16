package com.example.eventbookingapi.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        String location,
        LocalDateTime dateTime,
        Integer totalSeats,
        Integer availableSeats) {
}
