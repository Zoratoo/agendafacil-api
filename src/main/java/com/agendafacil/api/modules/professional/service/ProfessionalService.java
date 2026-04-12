package com.agendafacil.api.modules.professional.service;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.professional.dto.CreateProfessionalDTO;
import com.agendafacil.api.modules.professional.dto.ProfessionalResponseDTO;
import com.agendafacil.api.modules.professional.entity.Professional;
import com.agendafacil.api.modules.professional.repository.ProfessionalRepository;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.modules.user.repository.UserRepository;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {
    private final EstablishmentRepository establishmentRepository;
    private final ProfessionalRepository professionalRepository;
    private final UserRepository userRepository;

    public ProfessionalResponseDTO create(CreateProfessionalDTO createProfessionalDTO) {
        User user = userRepository.findById(createProfessionalDTO.getUserId())
            .orElseThrow(() -> new EntityNotFoundException(
                    "User not found with id: " + createProfessionalDTO.getUserId()
            ));

        Establishment establishment = establishmentRepository.findById(createProfessionalDTO.getEstablishmentId())
            .orElseThrow(() -> new EntityNotFoundException(
                    "Establishment not found with id: " + createProfessionalDTO.getEstablishmentId()
            ));

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!establishment.getOwner().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You don't have permission to create a professional for this establishment");
        }

        List<Establishment> establishments = new ArrayList<>();
        establishments.add(establishment);
        Professional professional = Professional.builder()
                .user(user)
                .establishments(establishments)
                .specialty(createProfessionalDTO.getSpecialty())
                .bio(createProfessionalDTO.getBio())
                .document(createProfessionalDTO.getDocument())
                .avatarUrl(createProfessionalDTO.getAvatarUrl())
                .build();

        Professional saved = professionalRepository.save(professional);

        return toResponseDTO(saved);
    }

    public List<ProfessionalResponseDTO> findByEstablishment(UUID establishmentId) {
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + establishmentId
        ));

        List<Professional> professionals = professionalRepository.findByEstablishments(establishment);
        return professionals.stream().map(this::toResponseDTO).toList();
    }

    public ProfessionalResponseDTO addToEstablishment(UUID professionalId, UUID establishmentId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found with id: " + professionalId
        ));

        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + establishmentId
        ));

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!establishment.getOwner().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("You don't have permission to add a professional in this establishment");
        }

        professional.getEstablishments().add(establishment);
        Professional saved = professionalRepository.save(professional);
        return toResponseDTO(saved);
    }

    public ProfessionalResponseDTO toResponseDTO(Professional professional) {
        return new ProfessionalResponseDTO(
            professional.getId(),
            new UserSummaryDTO(
                    professional.getUser().getId(),
                    professional.getUser().getName(),
                    professional.getUser().getEmail()
            ),
            professional.getEstablishments().stream().map(
                establishmentSummaryDTO -> new EstablishmentSummaryDTO(
                    establishmentSummaryDTO.getId(),
                    establishmentSummaryDTO.getName(),
                    establishmentSummaryDTO.getCategory(),
                    establishmentSummaryDTO.getPhone(),
                    establishmentSummaryDTO.getCep(),
                    establishmentSummaryDTO.getAddress(),
                    establishmentSummaryDTO.getNumber(),
                    establishmentSummaryDTO.getNeighborhood(),
                    establishmentSummaryDTO.getCity(),
                    establishmentSummaryDTO.getState(),
                    establishmentSummaryDTO.getCreatedAt(),
                    establishmentSummaryDTO.getUpdatedAt()
                )
            ).toList(),
            professional.getSpecialty(),
            professional.getBio(),
            professional.getDocument(),
            professional.getAvatarUrl(),
            professional.getActive(),
            professional.getCreatedAt(),
            professional.getUpdatedAt()
        );
    }
}
