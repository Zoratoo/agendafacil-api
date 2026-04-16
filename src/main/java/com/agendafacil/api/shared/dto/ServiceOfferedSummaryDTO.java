package com.agendafacil.api.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ServiceOfferedSummaryDTO {
    private UUID id;
    private UUID establishmentId;
    private String name;
    private String description;
    private int durationMinutes;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
