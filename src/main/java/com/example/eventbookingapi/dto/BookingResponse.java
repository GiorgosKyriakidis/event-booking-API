package com.example.eventbookingapi.dto;

import java.time.LocalDateTime;

public record BookingResponse(Long id,
                              Long eventId,
                              String customerName,
                              String customerEmail,
                              Integer seats,
                              LocalDateTime bookedAt) {
}
