package com.agendafacil.api.modules.schedule.entity;

import com.agendafacil.api.modules.professional.entity.Professional;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "blocked_slots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BlockedSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @Column(nullable = false)
    private LocalDate blockedDate;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column(length = 255)
    private String reason;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
