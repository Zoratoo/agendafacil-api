package com.agendafacil.api.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EstablishmentSummaryDTO {
    private UUID id;
    private String name;
    private String category;
    private String phone;
    private String cep;
    private String address;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
