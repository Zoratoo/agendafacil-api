package com.agendafacil.api.modules.professional.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.professional.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProfessionalRepository extends JpaRepository<Professional, UUID> {
    List<Professional> findByEstablishments(Establishment establishment);
}
