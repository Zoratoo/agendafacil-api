package com.agendafacil.api.modules.establishment.service;

import com.agendafacil.api.modules.establishment.dto.CreateEstablishmentDTO;
import com.agendafacil.api.modules.establishment.dto.EstablishmentResponseDTO;
import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.establishment.repository.EstablishmentUserRepository;
import com.agendafacil.api.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstablishmentService {
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentUserRepository establishmentUserRepository;

    public EstablishmentResponseDTO create(CreateEstablishmentDTO createEstablishmentDTO) {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Establishment establishment = Establishment.builder()
            .name(createEstablishmentDTO.getName())
            .category(createEstablishmentDTO.getCategory())
            .phone(createEstablishmentDTO.getPhone())
            .cep(createEstablishmentDTO.getCep())
            .address(createEstablishmentDTO.getAddress())
            .number(createEstablishmentDTO.getNumber())
            .neighborhood(createEstablishmentDTO.getNeighborhood())
            .city(createEstablishmentDTO.getCity())
            .state(createEstablishmentDTO.getState())
            .build();

        Establishment saved = establishmentRepository.save(establishment);

        establishmentUserRepository.save(
            EstablishmentUser.builder()
            .role(EstablishmentRole.OWNER)
            .user(userAuthenticated)
            .establishment(saved)
            .build()
        );
        return toResponseDTO(saved);
    }

    public List<EstablishmentResponseDTO> findMyEstablishments() {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EstablishmentUser> establishmentUsers = establishmentUserRepository.findByUser(userAuthenticated);

        return establishmentUsers.stream()
                .map(eu -> toResponseDTO(eu.getEstablishment()))
                .toList();
    }

    private EstablishmentResponseDTO toResponseDTO(Establishment establishment) {
        return new EstablishmentResponseDTO(
            establishment.getId(),
            establishment.getName(),
            establishment.getCategory(),
            establishment.getPhone(),
            establishment.getCep(),
            establishment.getAddress(),
            establishment.getNumber(),
            establishment.getNeighborhood(),
            establishment.getCity(),
            establishment.getState(),
            establishment.getActive(),
            establishment.getCreatedAt(),
            establishment.getUpdatedAt()
        );
    }
}
