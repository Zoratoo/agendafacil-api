package com.agendafacil.api.modules.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateBlockedSlotDTO {
    @NotBlank
    private LocalDate blockedDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
}
