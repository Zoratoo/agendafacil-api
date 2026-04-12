package com.agendafacil.api.modules.establishment.service;

import com.agendafacil.api.modules.establishment.dto.CreateEstablishmentDTO;
import com.agendafacil.api.modules.establishment.dto.EstablishmentResponseDTO;
import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstablishmentService {
    private final EstablishmentRepository establishmentRepository;

    public EstablishmentResponseDTO create(CreateEstablishmentDTO createEstablishmentDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Establishment establishment = Establishment.builder()
                .owner(user)
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
        return new EstablishmentResponseDTO(
            saved.getId(),
            saved.getName(),
            saved.getCategory(),
            saved.getPhone(),
            saved.getCep(),
            saved.getAddress(),
            saved.getNumber(),
            saved.getNeighborhood(),
            saved.getCity(),
            saved.getState(),
            saved.getActive(),
            saved.getCreatedAt(),
            saved.getUpdatedAt()
        );
    }

    public List<EstablishmentResponseDTO> findByOwner() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Establishment> establishments = establishmentRepository.findByOwner(user);
        return establishments.stream().map(
                establishment -> new EstablishmentResponseDTO(
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
                )
        ).toList();
    }

}
