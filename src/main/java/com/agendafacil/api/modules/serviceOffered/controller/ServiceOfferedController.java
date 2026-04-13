package com.agendafacil.api.modules.serviceOffered.controller;

import com.agendafacil.api.modules.serviceOffered.dto.CreateServiceOfferedDTO;
import com.agendafacil.api.modules.serviceOffered.dto.ServiceOfferedResponseDTO;
import com.agendafacil.api.modules.serviceOffered.service.ServiceOfferedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services-offered")
@RequiredArgsConstructor
public class ServiceOfferedController {
    private final ServiceOfferedService serviceOfferedService;
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ServiceOfferedResponseDTO create(@RequestBody CreateServiceOfferedDTO createServiceOfferedDTO) {
        return serviceOfferedService.create(createServiceOfferedDTO);
    }

    @GetMapping("establishments/{establishmentId}")
    @PreAuthorize("isAuthenticated()")
    public List<ServiceOfferedResponseDTO> findByEstablishment(@PathVariable UUID establishmentId) {
        return serviceOfferedService.findByEstablishment(establishmentId);
    }
}
