package com.agendafacil.api.modules.schedule.repository;

import com.agendafacil.api.modules.professional.entity.Professional;
import com.agendafacil.api.modules.schedule.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, UUID> {
    List<WorkingHours> findByProfessional(Professional professional);
}
