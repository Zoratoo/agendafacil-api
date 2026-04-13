package com.agendafacil.api.modules.schedule.dto;

import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WorkingHoursResponseDTO {
    private UUID id;
    private UserSummaryDTO user;
    private EstablishmentSummaryDTO establishmentSummaryDTO;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
