package com.agendafacil.api.modules.establishment.entity;

import com.agendafacil.api.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "establishment_users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"establishment_id", "user_id"})
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstablishmentUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstablishmentRole role;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
