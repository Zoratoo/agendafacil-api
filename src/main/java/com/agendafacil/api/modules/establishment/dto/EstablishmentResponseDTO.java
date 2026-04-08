package com.agendafacil.api.modules.establishment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EstablishmentResponseDTO {
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
    private Boolean active;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
