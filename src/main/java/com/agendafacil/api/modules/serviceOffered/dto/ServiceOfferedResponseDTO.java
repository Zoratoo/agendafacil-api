package com.agendafacil.api.modules.serviceOffered.dto;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ServiceOfferedResponseDTO {
    private UUID id;
    private EstablishmentSummaryDTO establishment;
    private String name;
    private String description;
    private int durationMinutes;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
