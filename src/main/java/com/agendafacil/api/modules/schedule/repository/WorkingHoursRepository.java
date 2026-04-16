package com.agendafacil.api.modules.schedule.repository;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.schedule.entity.WorkingHours;
import com.agendafacil.api.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, UUID> {
    List<WorkingHours> findByEstablishmentAndUser(Establishment establishment, User user);

    List<WorkingHours> findByUserAndEstablishmentAndDayOfWeek(User user, Establishment establishment, DayOfWeek dayOfWeek);
}
