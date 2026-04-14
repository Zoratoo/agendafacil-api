package com.agendafacil.api.modules.invitation.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.invitation.entity.EstablishmentInvitation;
import com.agendafacil.api.modules.invitation.entity.InvitationStatus;
import com.agendafacil.api.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstablishmentInvitationRepository extends JpaRepository<EstablishmentInvitation, UUID> {
    List<EstablishmentInvitation> findByInvitedUserAndStatus(User user, InvitationStatus status);
    Optional<EstablishmentInvitation> findByInvitedUserAndEstablishment(User user, Establishment establishment);
}
