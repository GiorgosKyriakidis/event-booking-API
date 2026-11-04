package com.example.eventbookingapi.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;


public record EventRequest(
        @NotBlank @Size(max = 255) String title,
        @Size(max = 255) String description,
        @NotBlank @Size(max = 255) String location,
        @NotNull @Future LocalDateTime dateTime,
        @NotNull @Min(1) Integer totalSeats
) {

}
