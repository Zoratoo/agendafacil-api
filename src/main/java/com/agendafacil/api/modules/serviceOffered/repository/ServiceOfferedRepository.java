package com.agendafacil.api.modules.serviceOffered.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.serviceOffered.entity.ServiceOffered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceOfferedRepository extends JpaRepository<ServiceOffered, UUID> {
    List<ServiceOffered> findByEstablishment(Establishment establishment);
}
