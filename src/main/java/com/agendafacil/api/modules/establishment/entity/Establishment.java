package com.agendafacil.api.modules.establishment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "establishments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Establishment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(length = 20)
    private String phone;

    @Column(length = 10)
    private String cep;

    @Column(length = 150)
    private String address;

    @Column(length = 10)
    private String number;

    @Column(length = 80)
    private String neighborhood;

    @Column(length = 80)
    private String city;

    @Column(length = 2)
    private String state;

    @Builder.Default()
    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
