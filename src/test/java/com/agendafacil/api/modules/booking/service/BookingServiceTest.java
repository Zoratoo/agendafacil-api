package com.agendafacil.api.modules.booking.service;

import com.agendafacil.api.modules.booking.dto.AvailableSlotsDTO;
import com.agendafacil.api.modules.booking.dto.CreateBookingDTO;
import com.agendafacil.api.modules.booking.entity.Booking;
import com.agendafacil.api.modules.booking.repository.BookingRepository;
import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.establishment.repository.EstablishmentUserRepository;
import com.agendafacil.api.modules.schedule.entity.WorkingHours;
import com.agendafacil.api.modules.schedule.repository.BlockedSlotRepository;
import com.agendafacil.api.modules.schedule.repository.WorkingHoursRepository;
import com.agendafacil.api.modules.serviceOffered.entity.ServiceOffered;
import com.agendafacil.api.modules.serviceOffered.repository.ServiceOfferedRepository;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EstablishmentRepository establishmentRepository;

    @Mock
    private EstablishmentUserRepository establishmentUserRepository;

    @Mock
    private ServiceOfferedRepository serviceOfferedRepository;

    @Mock
    private WorkingHoursRepository workingHoursRepository;

    @Mock
    private BlockedSlotRepository blockedSlotRepository;

    @Test
    void shouldReturnEmptyListWhenProfessionalHasNoWorkingHours() {
        // Arrange
        // cria um UUID para professionalId, establishmentId, serviceId
        UUID professionalId = UUID.randomUUID();
        UUID establishmentId = UUID.randomUUID();
        UUID serviceId = UUID.randomUUID();

        User professional = User.builder()
                .id(professionalId)
                .name("João")
                .email("joao@email.com")
                .build();

        Establishment establishment = Establishment.builder()
                .id(establishmentId)
                .name("Establishment test")
                .build();

        EstablishmentUser establishmentUser = EstablishmentUser.builder()
                .user(professional)
                .establishment(establishment)
                .role(EstablishmentRole.PROFESSIONAL)
                .build();

        ServiceOffered serviceOffered = ServiceOffered.builder()
                .id(serviceId)
                .establishment(establishment)
                .name("Service test")
                .build();

        when(userRepository.findById(professionalId))
                .thenReturn(Optional.of(professional));

        when(establishmentRepository.findById(establishmentId))
                .thenReturn(Optional.of(establishment));

        when(establishmentUserRepository.findByUserAndEstablishment(professional, establishment))
                .thenReturn(Optional.of(establishmentUser));

        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(serviceOffered));

        when(workingHoursRepository.findByUserAndEstablishmentAndDayOfWeek(professional, establishment, DayOfWeek.MONDAY))
                .thenReturn(new ArrayList<>());

        List<AvailableSlotsDTO> result = bookingService.getAvailableSlots(
                professionalId,
                establishmentId,
                serviceId,
                LocalDate.of(2026, 4, 20)
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenClientAlreadyHasBookingAtSameTime() {
        UUID professionalId = UUID.randomUUID();
        UUID establishmentId = UUID.randomUUID();
        UUID serviceId = UUID.randomUUID();

        User client = User.builder()
                .id(UUID.randomUUID())
                .name("Cliente")
                .email("cliente@email.com")
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(client, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        User professional = User.builder()
                .id(professionalId)
                .name("João")
                .email("joao@email.com")
                .build();

        Establishment establishment = Establishment.builder()
                .id(establishmentId)
                .name("Establishment test")
                .build();

        EstablishmentUser establishmentUser = EstablishmentUser.builder()
                .user(professional)
                .establishment(establishment)
                .role(EstablishmentRole.PROFESSIONAL)
                .build();

        ServiceOffered serviceOffered = ServiceOffered.builder()
                .id(serviceId)
                .establishment(establishment)
                .name("Service test")
                .durationMinutes(30)
                .build();

        Booking existingBooking = Booking.builder()
                .client(client)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 30))
                .build();

        when(bookingRepository.findByClientAndBookingDateAndStatus(any(), any(), any()))
                .thenReturn(List.of(existingBooking));

        when(bookingRepository.findByProfessionalAndBookingDateAndStatus(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        when(userRepository.findById(professionalId))
                .thenReturn(Optional.of(professional));

        when(establishmentRepository.findById(establishmentId))
                .thenReturn(Optional.of(establishment));

        when(establishmentUserRepository.findByUserAndEstablishment(professional, establishment))
                .thenReturn(Optional.of(establishmentUser));

        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(serviceOffered));

        CreateBookingDTO createBookingDTO = new CreateBookingDTO(
            establishmentId,
            serviceId,
            professionalId,
            LocalDate.now(),
            LocalTime.of(9, 0)
        );

        assertThrows(IllegalStateException.class, () -> {
            bookingService.create(createBookingDTO);
        });
    }

    @Test
    void shouldReturnAvailableSlotsWhenProfessionalHasWorkingHours() {
        UUID professionalId = UUID.randomUUID();
        UUID establishmentId = UUID.randomUUID();
        UUID serviceId = UUID.randomUUID();

        User professional = User.builder()
                .id(professionalId)
                .name("João")
                .email("joao@email.com")
                .build();

        Establishment establishment = Establishment.builder()
                .id(establishmentId)
                .name("Establishment test")
                .build();

        EstablishmentUser establishmentUser = EstablishmentUser.builder()
                .user(professional)
                .establishment(establishment)
                .role(EstablishmentRole.PROFESSIONAL)
                .build();

        ServiceOffered serviceOffered = ServiceOffered.builder()
                .id(serviceId)
                .establishment(establishment)
                .name("Service test")
                .durationMinutes(60)
                .build();

        WorkingHours workingHours = WorkingHours.builder()
                .user(professional)
                .establishment(establishment)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        when(userRepository.findById(professionalId))
                .thenReturn(Optional.of(professional));

        when(establishmentRepository.findById(establishmentId))
                .thenReturn(Optional.of(establishment));

        when(establishmentUserRepository.findByUserAndEstablishment(professional, establishment))
                .thenReturn(Optional.of(establishmentUser));

        when(serviceOfferedRepository.findById(serviceId))
                .thenReturn(Optional.of(serviceOffered));

        when(workingHoursRepository.findByUserAndEstablishmentAndDayOfWeek(professional, establishment, DayOfWeek.MONDAY))
                .thenReturn(List.of(workingHours));

        when(blockedSlotRepository.findByUserAndEstablishmentAndBlockedDate(professional, establishment, LocalDate.of(2026, 4, 20)))
                .thenReturn(new ArrayList<>());

        when(bookingRepository.findByProfessionalAndBookingDate(professional, LocalDate.of(2026, 4, 20)))
                .thenReturn(new ArrayList<>());

        List<AvailableSlotsDTO> result = bookingService.getAvailableSlots(
                professionalId,
                establishmentId,
                serviceId,
                LocalDate.of(2026, 4, 20)
        );

        assertEquals(2, result.size());
        assertEquals(LocalTime.of(9, 0), result.get(0).getStartTime());
        assertEquals(LocalTime.of(10, 0), result.get(1).getStartTime());
    }
}