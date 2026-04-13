package com.agendafacil.api.modules.schedule.service;

import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.establishment.repository.EstablishmentUserRepository;
import com.agendafacil.api.modules.schedule.dto.BlockedSlotResponseDTO;
import com.agendafacil.api.modules.schedule.dto.CreateBlockedSlotDTO;
import com.agendafacil.api.modules.schedule.dto.CreateWorkingHoursDTO;
import com.agendafacil.api.modules.schedule.dto.WorkingHoursResponseDTO;
import com.agendafacil.api.modules.schedule.entity.BlockedSlot;
import com.agendafacil.api.modules.schedule.entity.WorkingHours;
import com.agendafacil.api.modules.schedule.repository.BlockedSlotRepository;
import com.agendafacil.api.modules.schedule.repository.WorkingHoursRepository;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.modules.user.repository.UserRepository;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final BlockedSlotRepository blockedSlotRepository;
    private final WorkingHoursRepository workingHoursRepository;
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentUserRepository establishmentUserRepository;
    private final UserRepository userRepository;

    public WorkingHoursResponseDTO addWorkingHours(CreateWorkingHoursDTO createWorkingHoursDTO) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Establishment establishment = establishmentRepository.findById(createWorkingHoursDTO.getEstablishmentId())
                .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + createWorkingHoursDTO.getEstablishmentId()));

        EstablishmentUser establishmentUsers = establishmentUserRepository
                .findByUserAndEstablishment(authenticatedUser, establishment)
                .orElseThrow(() -> new EntityNotFoundException("You aren't a professional in this establishment"));

        if(establishmentUsers.getRole() != EstablishmentRole.PROFESSIONAL)
            throw new AccessDeniedException("Only professionals can create working hours for this establishment");

        WorkingHours workingHours = WorkingHours.builder()
            .user(authenticatedUser)
            .establishment(establishment)
            .dayOfWeek(createWorkingHoursDTO.getDayOfWeek())
            .startTime(createWorkingHoursDTO.getStartTime())
            .endTime(createWorkingHoursDTO.getEndTime())
            .build();

        WorkingHours saved = workingHoursRepository.save(workingHours);
        return workingHoursToResponse(saved);
    }

    public BlockedSlotResponseDTO addBlockedSlot(CreateBlockedSlotDTO createBlockedSlotDTO) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Establishment establishment = establishmentRepository.findById(createBlockedSlotDTO.getEstablishmentId())
                .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + createBlockedSlotDTO.getEstablishmentId()));

        EstablishmentUser establishmentUsers = establishmentUserRepository
                .findByUserAndEstablishment(authenticatedUser, establishment)
                .orElseThrow(() -> new EntityNotFoundException("You aren't a professional in this establishment"));

        if(establishmentUsers.getRole() != EstablishmentRole.PROFESSIONAL)
            throw new AccessDeniedException("Only professionals can create working hours for this establishment");

        BlockedSlot blockedSlot = BlockedSlot.builder()
                .user(authenticatedUser)
                .establishment(establishment)
                .blockedDate(createBlockedSlotDTO.getBlockedDate())
                .startTime(createBlockedSlotDTO.getStartTime())
                .endTime(createBlockedSlotDTO.getEndTime())
                .reason(createBlockedSlotDTO.getReason())
                .build();

        BlockedSlot saved = blockedSlotRepository.save(blockedSlot);
        return blockedSlotToResponse(saved);
    }

    public List<WorkingHoursResponseDTO> findMyEstablishmentWorkingHours(UUID establishmentId) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + establishmentId));

        EstablishmentUser establishmentUsers = establishmentUserRepository
                .findByUserAndEstablishment(authenticatedUser, establishment)
                .orElseThrow(() -> new EntityNotFoundException("You aren't a professional in this establishment"));

        if(establishmentUsers.getRole() != EstablishmentRole.PROFESSIONAL)
            throw new AccessDeniedException("Only professionals can create working hours for this establishment");

        List<WorkingHours> workingHours = workingHoursRepository.findByEstablishmentAndUser(establishment, authenticatedUser);
        return workingHours.stream().map(this::workingHoursToResponse).toList();
    }

    public List<WorkingHoursResponseDTO> findWorkingHoursByProfessionalAndEstablishment(UUID userId, UUID establishmentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Professional not found with id: " + userId));

        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + establishmentId));

        EstablishmentUser establishmentUsers = establishmentUserRepository.findByUserAndEstablishment(user, establishment)
                .orElseThrow(() -> new EntityNotFoundException("User aren't a professional in this establishment"));

        if(establishmentUsers.getRole() != EstablishmentRole.PROFESSIONAL)
            throw new AccessDeniedException("Only professionals have a working hours for this establishment");


        List<WorkingHours> workingHours = workingHoursRepository.findByEstablishmentAndUser(establishment, user);
        return workingHours.stream().map(this::workingHoursToResponse).toList();
    }

    private WorkingHoursResponseDTO workingHoursToResponse(WorkingHours workingHours) {
        return new WorkingHoursResponseDTO(
            workingHours.getId(),
            new UserSummaryDTO(
                workingHours.getUser().getId(),
                workingHours.getUser().getName(),
                workingHours.getUser().getEmail()
            ),
            new EstablishmentSummaryDTO(
                workingHours.getEstablishment().getId(),
                workingHours.getEstablishment().getName(),
                workingHours.getEstablishment().getCategory(),
                workingHours.getEstablishment().getPhone(),
                workingHours.getEstablishment().getCep(),
                workingHours.getEstablishment().getAddress(),
                workingHours.getEstablishment().getNumber(),
                workingHours.getEstablishment().getCity(),
                workingHours.getEstablishment().getNeighborhood(),
                workingHours.getEstablishment().getState(),
                workingHours.getEstablishment().getCreatedAt(),
                workingHours.getEstablishment().getUpdatedAt()
            ),
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
            new UserSummaryDTO(
                    blockedSlot.getUser().getId(),
                    blockedSlot.getUser().getName(),
                    blockedSlot.getUser().getEmail()
            ),
            new EstablishmentSummaryDTO(
                    blockedSlot.getEstablishment().getId(),
                    blockedSlot.getEstablishment().getName(),
                    blockedSlot.getEstablishment().getCategory(),
                    blockedSlot.getEstablishment().getPhone(),
                    blockedSlot.getEstablishment().getCep(),
                    blockedSlot.getEstablishment().getAddress(),
                    blockedSlot.getEstablishment().getNumber(),
                    blockedSlot.getEstablishment().getCity(),
                    blockedSlot.getEstablishment().getNeighborhood(),
                    blockedSlot.getEstablishment().getState(),
                    blockedSlot.getEstablishment().getCreatedAt(),
                    blockedSlot.getEstablishment().getUpdatedAt()
            ),
            blockedSlot.getBlockedDate(),
            blockedSlot.getStartTime(),
            blockedSlot.getEndTime(),
            blockedSlot.getReason(),
            blockedSlot.getCreatedAt()
        );
    }
}
