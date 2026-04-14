package com.agendafacil.api.modules.invitation.dto;

import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.invitation.entity.InvitationStatus;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class InvitationResponseDTO {
    private UUID id;
    private EstablishmentSummaryDTO establishment;
    private UserSummaryDTO invited;
    private UserSummaryDTO invitedUser;
    private EstablishmentRole role;
    private InvitationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
