package com.agendafacil.api.modules.professional.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateProfessionalDTO {
    @NotBlank
    private UUID userId;
    @NotBlank
    private UUID establishmentId;
    private String specialty;
    private String bio;
    private String document;
    private String avatarUrl;
}
