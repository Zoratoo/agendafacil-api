package com.agendafacil.api.modules.professional.controller;

import com.agendafacil.api.modules.professional.dto.CreateProfessionalDTO;
import com.agendafacil.api.modules.professional.dto.ProfessionalResponseDTO;
import com.agendafacil.api.modules.professional.service.ProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professionals")
@RequiredArgsConstructor
public class ProfessionalController {
    private final ProfessionalService professionalService;

    @PostMapping()
    @PreAuthorize("hasAuthority('OWNER')")
    public ProfessionalResponseDTO create(@RequestBody CreateProfessionalDTO createProfessionalDTO) {
        return professionalService.create(createProfessionalDTO);
    }

    @GetMapping("/establishment/{establishmentId}")
    @PreAuthorize("hasAuthority('OWNER')")
    public List<ProfessionalResponseDTO> findByEstablishment(@PathVariable UUID establishmentId) {
        return professionalService.findByEstablishment(establishmentId);
    }

    @PostMapping("/{professionalId}/establishment/{establishmentId}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ProfessionalResponseDTO addToEstablishment(@PathVariable UUID professionalId, @PathVariable UUID establishmentId) {
        return professionalService.addToEstablishment(professionalId, establishmentId);
    }
}
