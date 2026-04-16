package com.agendafacil.api.modules.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AvailableSlotsDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
