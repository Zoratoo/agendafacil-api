package com.agendafacil.api.modules.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateBookingDTO {
    @NotNull
    private UUID establishmentId;
    @NotNull
    private UUID serviceId;
    @NotNull
    private UUID professionalId;
    @NotNull
    private LocalDate bookingDate;
    @NotNull
    private LocalTime startTime;
}
