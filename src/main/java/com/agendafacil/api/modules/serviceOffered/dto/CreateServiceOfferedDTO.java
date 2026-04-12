package com.agendafacil.api.modules.serviceOffered.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateServiceOfferedDTO {
    @NotBlank
    private UUID establishmentId;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private int durationMinutes;
    private double price;
}
