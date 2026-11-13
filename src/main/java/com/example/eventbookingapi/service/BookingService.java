package com.example.eventbookingapi.service;

import com.example.eventbookingapi.dto.BookingRequest;
import com.example.eventbookingapi.dto.BookingResponse;
import com.example.eventbookingapi.exception.NotEnoughSeatsException;
import com.example.eventbookingapi.exception.ResourceNotFoundException;
import com.example.eventbookingapi.model.Booking;
import com.example.eventbookingapi.model.Event;
import com.example.eventbookingapi.repository.BookingRepository;
import com.example.eventbookingapi.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;

    public BookingService(BookingRepository bookingRepository, EventRepository eventRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public BookingResponse createBooking(Long eventId, BookingRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        if (event.getAvailableSeats() < request.seats()) {
            throw new NotEnoughSeatsException(
                    "Only " + event.getAvailableSeats() + " seats left");
        }

        event.setAvailableSeats(event.getAvailableSeats() - request.seats());

        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setCustomerName(request.customerName());
        booking.setCustomerEmail(request.customerEmail());
        booking.setSeats(request.seats());
        booking.setBookedAt(LocalDateTime.now());

        return toResponse(bookingRepository.save(booking));
    }

    public List<BookingResponse> getBookingsForEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event not found with id: " + eventId);
        }
        return bookingRepository.findByEventId(eventId).stream()
                .map(this::toResponse)
                .toList();
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getId(), b.getEvent().getId(), b.getCustomerName(),
                b.getCustomerEmail(), b.getSeats(), b.getBookedAt());
    }
}