package com.example.eventbookingapi.controller;

import com.example.eventbookingapi.dto.BookingRequest;
import com.example.eventbookingapi.dto.BookingResponse;
import com.example.eventbookingapi.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @PathVariable Long eventId,
            @Valid @RequestBody BookingRequest request) {
        BookingResponse created = bookingService.createBooking(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<BookingResponse> getBookings(@PathVariable Long eventId) {
        return bookingService.getBookingsForEvent(eventId);
    }
}