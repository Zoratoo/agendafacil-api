package com.agendafacil.api.modules.schedule.dto;

import com.agendafacil.api.modules.professional.dto.ProfessionalResponseDTO;
import com.agendafacil.api.modules.professional.entity.Professional;
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
    private ProfessionalResponseDTO professional;
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private LocalDateTime createdAt;
}
