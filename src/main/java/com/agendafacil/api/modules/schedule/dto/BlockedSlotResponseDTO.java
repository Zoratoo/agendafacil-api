package com.agendafacil.api.modules.schedule.dto;

import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BlockedSlotResponseDTO {
    private UUID id;
    private UserSummaryDTO user;
    private EstablishmentSummaryDTO establishmentSummaryDTO;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private LocalDateTime createdAt;
}
