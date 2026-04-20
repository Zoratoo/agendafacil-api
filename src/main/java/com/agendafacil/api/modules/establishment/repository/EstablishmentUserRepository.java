package com.agendafacil.api.modules.establishment.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstablishmentUserRepository extends JpaRepository<EstablishmentUser, UUID> {
    Optional<EstablishmentUser> findByUserAndEstablishment(User user, Establishment establishment);
    List<EstablishmentUser> findByEstablishmentAndRole(Establishment establishment, EstablishmentRole role);
    List<EstablishmentUser> findByUser(User user);
}
