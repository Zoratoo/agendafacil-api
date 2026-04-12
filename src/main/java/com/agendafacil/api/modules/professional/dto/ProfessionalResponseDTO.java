package com.agendafacil.api.modules.professional.dto;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProfessionalResponseDTO {
    private UUID id;
    private UserSummaryDTO user;
    private List<EstablishmentSummaryDTO> establishments;
    private String speciality;
    private String bio;
    private String document;
    private String avatarUrl;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

