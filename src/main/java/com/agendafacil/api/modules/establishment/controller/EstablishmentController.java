package com.agendafacil.api.modules.establishment.controller;

import com.agendafacil.api.modules.establishment.dto.CreateEstablishmentDTO;
import com.agendafacil.api.modules.establishment.dto.EstablishmentResponseDTO;
import com.agendafacil.api.modules.establishment.service.EstablishmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
