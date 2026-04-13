package com.agendafacil.api.modules.serviceOffered.service;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.establishment.repository.EstablishmentUserRepository;
import com.agendafacil.api.modules.serviceOffered.dto.CreateServiceOfferedDTO;
import com.agendafacil.api.modules.serviceOffered.dto.ServiceOfferedResponseDTO;
import com.agendafacil.api.modules.serviceOffered.entity.ServiceOffered;
import com.agendafacil.api.modules.serviceOffered.repository.ServiceOfferedRepository;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceOfferedService {
    private final ServiceOfferedRepository serviceOfferedRepository;
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentUserRepository establishmentUserRepository;

    public ServiceOfferedResponseDTO create(CreateServiceOfferedDTO createServiceOfferedDTO) {
        Establishment establishment = establishmentRepository.findById(createServiceOfferedDTO.getEstablishmentId())
            .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + createServiceOfferedDTO.getEstablishmentId()
        ));

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EstablishmentUser establishmentUser = establishmentUserRepository
            .findByUserAndEstablishment(authenticatedUser, establishment)
            .orElseThrow(() -> new AccessDeniedException("You are not a member of this establishment"));

        if (establishmentUser.getRole() != EstablishmentRole.OWNER) {
            throw new AccessDeniedException("Only owners can create services for this establishment");
        }

        ServiceOffered saved = serviceOfferedRepository.save(
            ServiceOffered.builder()
                .establishment(establishment)
                .name(createServiceOfferedDTO.getName())
                .description(createServiceOfferedDTO.getDescription())
                .durationMinutes(createServiceOfferedDTO.getDurationMinutes())
                .price(createServiceOfferedDTO.getPrice())
                .build()
        );

        return toResponseDTO(saved);
    }

    public List<ServiceOfferedResponseDTO> findByEstablishment(UUID establishmentId) {
        Establishment establishment = establishmentRepository.findById(establishmentId)
            .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + establishmentId
        ));
        List<ServiceOffered> servicesOffered = serviceOfferedRepository.findByEstablishment(establishment);

        return servicesOffered.stream().map(this::toResponseDTO).toList();
    }

    private ServiceOfferedResponseDTO toResponseDTO(ServiceOffered serviceOffered) {
        return new ServiceOfferedResponseDTO(
            serviceOffered.getId(),
            new EstablishmentSummaryDTO(
                    serviceOffered.getEstablishment().getId(),
                    serviceOffered.getEstablishment().getName(),
                    serviceOffered.getEstablishment().getCategory(),
                    serviceOffered.getEstablishment().getPhone(),
                    serviceOffered.getEstablishment().getCep(),
                    serviceOffered.getEstablishment().getAddress(),
                    serviceOffered.getEstablishment().getNumber(),
                    serviceOffered.getEstablishment().getNeighborhood(),
                    serviceOffered.getEstablishment().getCity(),
                    serviceOffered.getEstablishment().getState(),
                    serviceOffered.getEstablishment().getCreatedAt(),
                    serviceOffered.getEstablishment().getUpdatedAt()
            ),
            serviceOffered.getName(),
            serviceOffered.getDescription(),
            serviceOffered.getDurationMinutes(),
            serviceOffered.getPrice(),
            serviceOffered.getCreatedAt(),
            serviceOffered.getUpdatedAt()
        );
    }
}
