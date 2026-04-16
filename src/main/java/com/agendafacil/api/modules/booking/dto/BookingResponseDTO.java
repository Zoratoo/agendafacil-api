package com.agendafacil.api.modules.booking.dto;

import com.agendafacil.api.modules.booking.entity.BookingStatus;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.ServiceOfferedSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BookingResponseDTO {
    private UUID id;
    private EstablishmentSummaryDTO establishment;
    private ServiceOfferedSummaryDTO serviceOffered;
    private UserSummaryDTO professional;
    private UserSummaryDTO client;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus bookingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
