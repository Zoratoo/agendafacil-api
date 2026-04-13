package com.agendafacil.api.modules.schedule.controller;


import com.agendafacil.api.modules.schedule.dto.BlockedSlotResponseDTO;
import com.agendafacil.api.modules.schedule.dto.CreateBlockedSlotDTO;
import com.agendafacil.api.modules.schedule.dto.CreateWorkingHoursDTO;
import com.agendafacil.api.modules.schedule.dto.WorkingHoursResponseDTO;
import com.agendafacil.api.modules.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/working-hours")
    @PreAuthorize("isAuthenticated()")
    public WorkingHoursResponseDTO createWorkingHours(@RequestBody CreateWorkingHoursDTO createWorkingHoursDTO) {
        return scheduleService.addWorkingHours(createWorkingHoursDTO);
    }

    @PostMapping("/blocked-slots")
    @PreAuthorize("isAuthenticated()")
    public BlockedSlotResponseDTO createBlockedSlot(@RequestBody CreateBlockedSlotDTO createBlockedSlotDTO) {
        return scheduleService.addBlockedSlot(createBlockedSlotDTO);
    }

    @GetMapping("/my/establishment/{establishmentId}/working-hours")
    @PreAuthorize("isAuthenticated()")
    public List<WorkingHoursResponseDTO> findMyWorkingHours(@PathVariable UUID establishmentId) {
        return scheduleService.findMyEstablishmentWorkingHours(establishmentId);
    }

    @GetMapping("/professional/{professionalId}/establishment/{establishmentId}/working-hours")
    @PreAuthorize("isAuthenticated()")
    public List<WorkingHoursResponseDTO> findWorkingHoursByProfessional(@PathVariable UUID professionalId, @PathVariable UUID establishmentId) {
        return scheduleService.findWorkingHoursByProfessionalAndEstablishment(professionalId, establishmentId);
    }
}
