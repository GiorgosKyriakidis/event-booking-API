package com.example.eventbookingapi.dto;

import java.time.LocalDateTime;

public record EventRequest(
        String title,
        String description,
        String location,
        LocalDateTime dateTime,
        Integer totalSeats
) {

}
