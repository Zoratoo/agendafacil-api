package com.agendafacil.api.modules.schedule.service;

import com.agendafacil.api.modules.professional.entity.Professional;
import com.agendafacil.api.modules.professional.repository.ProfessionalRepository;
import com.agendafacil.api.modules.professional.service.ProfessionalService;
import com.agendafacil.api.modules.schedule.dto.BlockedSlotResponseDTO;
import com.agendafacil.api.modules.schedule.dto.CreateBlockedSlotDTO;
import com.agendafacil.api.modules.schedule.dto.CreateWorkingHoursDTO;
import com.agendafacil.api.modules.schedule.dto.WorkingHoursResponseDTO;
import com.agendafacil.api.modules.schedule.entity.BlockedSlot;
import com.agendafacil.api.modules.schedule.entity.WorkingHours;
import com.agendafacil.api.modules.schedule.repository.BlockedSlotRepository;
import com.agendafacil.api.modules.schedule.repository.WorkingHoursRepository;
import com.agendafacil.api.modules.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ProfessionalService professionalService;

    private final ProfessionalRepository professionalRepository;

    private final BlockedSlotRepository blockedSlotRepository;
    private final WorkingHoursRepository workingHoursRepository;

    public WorkingHoursResponseDTO addWorkingHours(CreateWorkingHoursDTO createWorkingHoursDTO) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Professional professional = professionalRepository.findByUser(authenticatedUser)
            .orElseThrow(() -> new EntityNotFoundException("User is not a Professional"));

        WorkingHours workingHours = WorkingHours.builder()
            .professional(professional)
            .dayOfWeek(createWorkingHoursDTO.getDayOfWeek())
            .startTime(createWorkingHoursDTO.getStartTime())
            .endTime(createWorkingHoursDTO.getEndTime())
            .build();

        WorkingHours saved = workingHoursRepository.save(workingHours);
        return workingHoursToResponse(saved);
    }

    public BlockedSlotResponseDTO addBlockedSlot(CreateBlockedSlotDTO createBlockedSlotDTO) {
        User authenticathedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Professional professional = professionalRepository.findByUser(authenticathedUser)
                .orElseThrow(() -> new EntityNotFoundException("User is not a Professional"));

        BlockedSlot blockedSlot = BlockedSlot.builder()
                .professional(professional)
                .blockedDate(createBlockedSlotDTO.getBlockedDate())
                .startTime(createBlockedSlotDTO.getStartTime())
                .endTime(createBlockedSlotDTO.getEndTime())
                .reason(createBlockedSlotDTO.getReason())
                .build();

        BlockedSlot saved = blockedSlotRepository.save(blockedSlot);
        return blockedSlotToResponse(saved);
    }

    public List<WorkingHoursResponseDTO> findMyWorkingHours() {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Professional professional = professionalRepository.findByUser(userAuthenticated)
                .orElseThrow(() -> new EntityNotFoundException("User is not a Professional"));

        List<WorkingHours> workingHours = workingHoursRepository.findByProfessional(professional);
        return workingHours.stream().map(this::workingHoursToResponse).toList();
    }

    public List<WorkingHoursResponseDTO> findWorkingHoursByProfessional(UUID professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found with id: " + professionalId));

        List<WorkingHours> workingHours = workingHoursRepository.findByProfessional(professional);
        return workingHours.stream().map(this::workingHoursToResponse).toList();
    }

    private WorkingHoursResponseDTO workingHoursToResponse(WorkingHours workingHours) {
        return new WorkingHoursResponseDTO(
            workingHours.getId(),
            professionalService.toResponseDTO(workingHours.getProfessional()),
            workingHours.getDayOfWeek(),
            workingHours.getStartTime(),
            workingHours.getEndTime(),
            workingHours.getCreatedAt(),
            workingHours.getUpdatedAt()
        );
    }

    private BlockedSlotResponseDTO blockedSlotToResponse(BlockedSlot blockedSlot) {
        return new BlockedSlotResponseDTO(
            blockedSlot.getId(),
            professionalService.toResponseDTO(blockedSlot.getProfessional()),
            blockedSlot.getBlockedDate(),
            blockedSlot.getStartTime(),
            blockedSlot.getEndTime(),
            blockedSlot.getReason(),
            blockedSlot.getCreatedAt()
        );
    }
}
