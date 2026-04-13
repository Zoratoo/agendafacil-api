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
    @PreAuthorize("hasAuthority('PROFESSIONAL')")
    public WorkingHoursResponseDTO createWorkingHours(@RequestBody CreateWorkingHoursDTO createWorkingHoursDTO) {
        return scheduleService.addWorkingHours(createWorkingHoursDTO);
    }

    @PostMapping("/blocked-slots")
    @PreAuthorize("hasAuthority('PROFESSIONAL')")
    public BlockedSlotResponseDTO createBlockedSlot(@RequestBody CreateBlockedSlotDTO createBlockedSlotDTO) {
        return scheduleService.addBlockedSlot(createBlockedSlotDTO);
    }

    @GetMapping("/my/working-hours")
    @PreAuthorize("hasAuthority('PROFESSIONAL')")
    public List<WorkingHoursResponseDTO> findMyWorkingHours() {
        return scheduleService.findMyWorkingHours();
    }

    @GetMapping("/professional/{professionalId}/working-hours")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'OWNER', 'PROFESSIONAL', 'CLIENT')")
    public List<WorkingHoursResponseDTO> findWorkingHoursByProfessional(@PathVariable UUID professionalId) {
        return scheduleService.findWorkingHoursByProfessional(professionalId);
    }
}
