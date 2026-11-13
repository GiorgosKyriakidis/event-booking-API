package com.example.eventbookingapi.dto;

import jakarta.validation.constraints.*;

public record BookingRequest(@NotBlank @Size(max = 255) String customerName,
                             @NotBlank @Email @Size(max = 255) String customerEmail,
                             @NotNull @Min(1) Integer seats) {
}
