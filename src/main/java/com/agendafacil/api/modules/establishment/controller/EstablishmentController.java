package com.agendafacil.api.modules.establishment.controller;

import com.agendafacil.api.modules.establishment.dto.CreateEstablishmentDTO;
import com.agendafacil.api.modules.establishment.dto.EstablishmentResponseDTO;
import com.agendafacil.api.modules.establishment.service.EstablishmentService;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/establishments")
@RequiredArgsConstructor
public class EstablishmentController {
    private final EstablishmentService establishmentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public EstablishmentResponseDTO create(@RequestBody @Valid CreateEstablishmentDTO createEstablishmentDTO) {
        return establishmentService.create(createEstablishmentDTO);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<EstablishmentResponseDTO> findMyEstablishments() {
        return establishmentService.findMyEstablishments();
    }

    @GetMapping("/{establishmentId}/professionals")
    @PreAuthorize("isAuthenticated()")
    public List<UserSummaryDTO> findProfessionalsByEstablishment(@PathVariable UUID establishmentId) {
        return establishmentService.findProfessionalsByEstablishment(establishmentId);
    }
}
