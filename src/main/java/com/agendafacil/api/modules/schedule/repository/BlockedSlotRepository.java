package com.agendafacil.api.modules.schedule.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.schedule.entity.BlockedSlot;
import com.agendafacil.api.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BlockedSlotRepository extends JpaRepository<BlockedSlot, UUID> {
    List<BlockedSlot> findByUserAndBlockedDate(User user, LocalDate blockedDate);

    List<BlockedSlot> findByUserAndEstablishmentAndBlockedDate(User user, Establishment establishment, LocalDate blockedDate);
}
