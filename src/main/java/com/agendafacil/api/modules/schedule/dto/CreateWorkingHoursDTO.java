package com.agendafacil.api.modules.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateWorkingHoursDTO {
    @NotBlank
    private UUID establishmentId;
    @NotBlank
    private DayOfWeek dayOfWeek;
    @NotBlank
    private LocalTime startTime;
    @NotBlank
    private LocalTime endTime;
}
