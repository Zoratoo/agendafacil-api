package com.agendafacil.api.modules.schedule.dto;

import com.agendafacil.api.modules.professional.dto.ProfessionalResponseDTO;
import com.agendafacil.api.modules.professional.entity.Professional;
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
    private ProfessionalResponseDTO professional;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
