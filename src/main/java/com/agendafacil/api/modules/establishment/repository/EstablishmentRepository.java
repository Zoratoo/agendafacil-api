package com.agendafacil.api.modules.establishment.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EstablishmentRepository extends JpaRepository<Establishment, UUID> {
}
