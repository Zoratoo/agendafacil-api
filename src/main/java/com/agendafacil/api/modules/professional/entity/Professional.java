package com.agendafacil.api.modules.professional.entity;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "professionals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Professional {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "establishment_professionals",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "establishment_id")
    )
    private List<Establishment> establishments;

    @Column(length = 100)
    private String specialty;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 50)
    private String document;

    @Column
    private String avatar_url;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
}
