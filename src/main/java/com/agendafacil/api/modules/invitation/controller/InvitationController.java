package com.agendafacil.api.modules.invitation.controller;

import com.agendafacil.api.modules.invitation.dto.CreateInvitationDTO;
import com.agendafacil.api.modules.invitation.dto.InvitationResponseDTO;
import com.agendafacil.api.modules.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public InvitationResponseDTO invite(@RequestBody CreateInvitationDTO createInvitationDTO) {
        return invitationService.invite(createInvitationDTO);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public List<InvitationResponseDTO> findMyInvitations() {
        return invitationService.findMyInvitations();
    }

    @PatchMapping("/{invitationId}/accept")
    @PreAuthorize("isAuthenticated()")
    public InvitationResponseDTO accept(@PathVariable UUID invitationId) {
        return invitationService.accept(invitationId);
    }

    @PatchMapping("/{invitationId}/reject")
    @PreAuthorize("isAuthenticated()")
    public InvitationResponseDTO reject(@PathVariable UUID invitationId) {
        return invitationService.reject(invitationId);
    }
}
