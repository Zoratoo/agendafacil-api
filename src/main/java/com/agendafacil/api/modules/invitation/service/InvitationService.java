package com.agendafacil.api.modules.invitation.service;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.establishment.repository.EstablishmentUserRepository;
import com.agendafacil.api.modules.invitation.dto.CreateInvitationDTO;
import com.agendafacil.api.modules.invitation.dto.InvitationResponseDTO;
import com.agendafacil.api.modules.invitation.entity.EstablishmentInvitation;
import com.agendafacil.api.modules.invitation.entity.InvitationStatus;
import com.agendafacil.api.modules.invitation.repository.EstablishmentInvitationRepository;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.modules.user.repository.UserRepository;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final EstablishmentInvitationRepository establishmentInvitationRepository;
    private final EstablishmentRepository establishmentRepository;
    private final UserRepository userRepository;
    private final EstablishmentUserRepository establishmentUserRepository;

    public InvitationResponseDTO invite(CreateInvitationDTO createInvitationDTO) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Establishment establishment = establishmentRepository.findById(createInvitationDTO.getEstablishmentId())
            .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + createInvitationDTO.getEstablishmentId()));

        EstablishmentUser establishmentUser = establishmentUserRepository.findByUserAndEstablishment(authenticatedUser, establishment)
            .orElseThrow(() -> new EntityNotFoundException("User or Establishment not founded"));

        if (establishmentUser.getRole() != EstablishmentRole.OWNER) {
            throw new AccessDeniedException("Only establishment's owners can invite");
        }

        User invitedUser = userRepository.findByEmail(createInvitationDTO.getInvitedUserEmail())
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + createInvitationDTO.getInvitedUserEmail()));

        establishmentUserRepository
            .findByUserAndEstablishment(invitedUser, establishment)
            .ifPresent(eu -> {
                if(eu.getRole() == EstablishmentRole.OWNER) {
                    throw new IllegalStateException("User is already an owner of this establishment");
                }
                if (eu.getRole() == createInvitationDTO.getRole()) {
                    throw new IllegalStateException("User already has this role in this establishment");
                }
            });

        establishmentInvitationRepository
            .findByInvitedUserAndEstablishment(invitedUser, establishment)
            .ifPresent(inv -> {
                if (inv.getStatus() == InvitationStatus.PENDING) {
                    throw new IllegalStateException("User already has a pending invitation for this establishment");
                }
            });

        EstablishmentInvitation establishmentInvitation = EstablishmentInvitation.builder()
            .establishment(establishment)
            .invitedBy(authenticatedUser)
            .invitedUser(invitedUser)
            .role(createInvitationDTO.getRole())
            .status(InvitationStatus.PENDING)
            .build();

        EstablishmentInvitation saved = establishmentInvitationRepository.save(establishmentInvitation);
        return toResponseDTO(saved);
    }

    public List<InvitationResponseDTO> findMyInvitations() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EstablishmentInvitation> establishmentInvitations = establishmentInvitationRepository.findByInvitedUserAndStatus(authenticatedUser, InvitationStatus.PENDING);
        return establishmentInvitations.stream().map(this::toResponseDTO).toList();
    }

    @Transactional
    public InvitationResponseDTO accept(UUID establishment_invitation_id) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EstablishmentInvitation establishmentInvitation = establishmentInvitationRepository.findById(establishment_invitation_id)
            .orElseThrow(() -> new EntityNotFoundException("Invitation don't exist"));

        if(!authenticatedUser.getId().equals(establishmentInvitation.getInvitedUser().getId())) {
            throw new AccessDeniedException("You cannot accept this invitation");
        }

        establishmentInvitation.setStatus(InvitationStatus.ACCEPTED);
        EstablishmentInvitation saved = establishmentInvitationRepository.save(establishmentInvitation);

        EstablishmentUser establishmentUser = EstablishmentUser.builder()
            .user(authenticatedUser)
            .establishment(establishmentInvitation.getEstablishment())
            .role(establishmentInvitation.getRole())
            .build();
        establishmentUserRepository.save(establishmentUser);

        return toResponseDTO(saved);
    }

    public InvitationResponseDTO reject(UUID establishment_invitation_id) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EstablishmentInvitation establishmentInvitation = establishmentInvitationRepository.findById(establishment_invitation_id)
            .orElseThrow(() -> new EntityNotFoundException("Invitation don't exist"));

        if(!authenticatedUser.getId().equals(establishmentInvitation.getInvitedUser().getId())) {
            throw new AccessDeniedException("You cannot reject this invitation");
        }

        establishmentInvitation.setStatus(InvitationStatus.REJECTED);
        EstablishmentInvitation saved = establishmentInvitationRepository.save(establishmentInvitation);
        return toResponseDTO(saved);
    }

    private InvitationResponseDTO toResponseDTO(EstablishmentInvitation establishmentInvitation) {
        return new InvitationResponseDTO(
            establishmentInvitation.getId(),
            new EstablishmentSummaryDTO(
                establishmentInvitation.getEstablishment().getId(),
                establishmentInvitation.getEstablishment().getName(),
                establishmentInvitation.getEstablishment().getCategory(),
                establishmentInvitation.getEstablishment().getPhone(),
                establishmentInvitation.getEstablishment().getCep(),
                establishmentInvitation.getEstablishment().getAddress(),
                establishmentInvitation.getEstablishment().getNumber(),
                establishmentInvitation.getEstablishment().getNeighborhood(),
                establishmentInvitation.getEstablishment().getCity(),
                establishmentInvitation.getEstablishment().getState(),
                establishmentInvitation.getEstablishment().getCreatedAt(),
                establishmentInvitation.getEstablishment().getUpdatedAt()
            ),
            new UserSummaryDTO(
                establishmentInvitation.getInvitedBy().getId(),
                establishmentInvitation.getInvitedBy().getName(),
                establishmentInvitation.getInvitedBy().getEmail()
            ),
            new UserSummaryDTO(
                establishmentInvitation.getInvitedUser().getId(),
                establishmentInvitation.getInvitedUser().getName(),
                establishmentInvitation.getInvitedUser().getEmail()
            ),
            establishmentInvitation.getRole(),
            establishmentInvitation.getStatus(),
            establishmentInvitation.getCreatedAt(),
            establishmentInvitation.getUpdatedAt()
        );
    }
}
