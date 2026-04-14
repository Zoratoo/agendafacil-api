package com.agendafacil.api.modules.invitation.dto;

import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateInvitationDTO {
    @NotBlank
    private String invitedUserEmail;
    @NotNull
    private UUID establishmentId;
    @NotNull
    private EstablishmentRole role;
}
