package com.agendafacil.api.modules.schedule.repository;

import com.agendafacil.api.modules.professional.entity.Professional;
import com.agendafacil.api.modules.schedule.entity.BlockedSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BlockedSlotRepository extends JpaRepository<BlockedSlot, UUID> {
    List<BlockedSlot> findByProfessionalAndBlockedDate(Professional professional, LocalDate blockedDate);
}
