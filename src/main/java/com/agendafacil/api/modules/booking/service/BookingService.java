package com.agendafacil.api.modules.booking.service;

import com.agendafacil.api.modules.booking.dto.AvailableSlotsDTO;
import com.agendafacil.api.modules.booking.dto.BookingResponseDTO;
import com.agendafacil.api.modules.booking.dto.CreateBookingDTO;
import com.agendafacil.api.modules.booking.entity.Booking;
import com.agendafacil.api.modules.booking.entity.BookingStatus;
import com.agendafacil.api.modules.booking.repository.BookingRepository;
import com.agendafacil.api.modules.establishment.entity.Establishment;
import com.agendafacil.api.modules.establishment.entity.EstablishmentRole;
import com.agendafacil.api.modules.establishment.entity.EstablishmentUser;
import com.agendafacil.api.modules.establishment.repository.EstablishmentRepository;
import com.agendafacil.api.modules.establishment.repository.EstablishmentUserRepository;
import com.agendafacil.api.modules.schedule.entity.BlockedSlot;
import com.agendafacil.api.modules.schedule.entity.WorkingHours;
import com.agendafacil.api.modules.schedule.repository.BlockedSlotRepository;
import com.agendafacil.api.modules.schedule.repository.WorkingHoursRepository;
import com.agendafacil.api.modules.serviceOffered.entity.ServiceOffered;
import com.agendafacil.api.modules.serviceOffered.repository.ServiceOfferedRepository;
import com.agendafacil.api.modules.user.entity.User;
import com.agendafacil.api.modules.user.repository.UserRepository;
import com.agendafacil.api.shared.dto.EstablishmentSummaryDTO;
import com.agendafacil.api.shared.dto.ServiceOfferedSummaryDTO;
import com.agendafacil.api.shared.dto.UserSummaryDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentUserRepository establishmentUserRepository;
    private final ServiceOfferedRepository serviceOfferedRepository;
    private final WorkingHoursRepository workingHoursRepository;
    private final BlockedSlotRepository blockedSlotRepository;

    public List<AvailableSlotsDTO> getAvailableSlots(UUID professionalId, UUID establishmentId, UUID serviceId, LocalDate date) {
        List<AvailableSlotsDTO> availableSlots = new ArrayList<>();

        User professional = userRepository.findById(professionalId)
            .orElseThrow(() -> new EntityNotFoundException("Professional not found with id: " + professionalId));

        Establishment establishment = establishmentRepository.findById(establishmentId)
            .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + establishmentId));

        EstablishmentUser establishmentUser = establishmentUserRepository.findByUserAndEstablishment(professional, establishment)
            .orElseThrow(() -> new EntityNotFoundException("User don't have relation in this establishment"));

        if (establishmentUser.getRole() != EstablishmentRole.PROFESSIONAL)
            throw new AccessDeniedException("This user is not a Professional in this establishment");

        ServiceOffered serviceOffered = serviceOfferedRepository.findById(serviceId)
            .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + serviceId));

        List<WorkingHours> workingHours = workingHoursRepository.findByUserAndEstablishmentAndDayOfWeek(professional, establishment, date.getDayOfWeek());
        if(workingHours.isEmpty()) return availableSlots;

        List<BlockedSlot> blockedSlots = blockedSlotRepository.findByUserAndEstablishmentAndBlockedDate(professional, establishment, date);
        boolean fullDayBlocked = blockedSlots.stream()
            .anyMatch(b -> b.getStartTime() == null);
        if (fullDayBlocked) return availableSlots;

        List<Booking> professionalBookings = bookingRepository.findByProfessionalAndBookingDate(professional, date);

        workingHours.forEach((wHours) -> {
            LocalTime startTime = wHours.getStartTime();
            LocalTime endTime = startTime.plusMinutes(serviceOffered.getDurationMinutes());
            while(!endTime.isAfter(wHours.getEndTime())) {
                LocalTime slotStart = startTime;
                LocalTime slotEnd = endTime;

                boolean isBlocked = blockedSlots.stream().anyMatch(blocked ->
                    hasTimeOverlap(slotStart, slotEnd, blocked.getStartTime(), blocked.getEndTime())
                );

                boolean hasBooking = professionalBookings.stream().anyMatch(booking ->
                    hasTimeOverlap(slotStart, slotEnd, booking.getStartTime(), booking.getEndTime())
                );

                if (!isBlocked && !hasBooking) {
                    availableSlots.add(new AvailableSlotsDTO(date, startTime, endTime));
                }

                startTime = endTime;
                endTime = endTime.plusMinutes(serviceOffered.getDurationMinutes());
            }
        });

        return availableSlots;
    }

    public BookingResponseDTO create(CreateBookingDTO createBookingDTO) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User professional = userRepository.findById(createBookingDTO.getProfessionalId())
            .orElseThrow(() -> new EntityNotFoundException("Professional not found with id: " + createBookingDTO.getProfessionalId()));

        Establishment establishment = establishmentRepository.findById(createBookingDTO.getEstablishmentId())
            .orElseThrow(() -> new EntityNotFoundException("Establishment not found with id: " + createBookingDTO.getEstablishmentId()));

        EstablishmentUser establishmentUser = establishmentUserRepository.findByUserAndEstablishment(professional, establishment)
            .orElseThrow(() -> new EntityNotFoundException("User don't have relation in this establishment"));

        if (establishmentUser.getRole() != EstablishmentRole.PROFESSIONAL)
            throw new AccessDeniedException("This user is not a Professional in this establishment");

        ServiceOffered serviceOffered = serviceOfferedRepository.findById(createBookingDTO.getServiceId())
            .orElseThrow(() -> new EntityNotFoundException("Service not found with id: " + createBookingDTO.getServiceId()));

        List<Booking> professionalBookings = bookingRepository.findByProfessionalAndBookingDateAndStatus(
            professional,
            createBookingDTO.getBookingDate(),
            BookingStatus.CONFIRMED
        );

        LocalTime slotEnd = createBookingDTO.getStartTime().plusMinutes(serviceOffered.getDurationMinutes());
        professionalBookings.forEach((professionalBooking) -> {
            LocalTime slotStart = createBookingDTO.getStartTime();

            LocalTime startTime = professionalBooking.getStartTime();
            LocalTime endTime = professionalBooking.getEndTime();
            if(hasTimeOverlap(slotStart, slotEnd, startTime, endTime))
                throw new IllegalStateException("Time slot not available");
        });

        List<Booking> clientBookings = bookingRepository.findByClientAndBookingDateAndStatus(
            authenticatedUser,
            createBookingDTO.getBookingDate(),
            BookingStatus.CONFIRMED
        );

        clientBookings.forEach((clientBooking) -> {
            LocalTime slotStart = createBookingDTO.getStartTime();

            LocalTime startTime = clientBooking.getStartTime();
            LocalTime endTime = clientBooking.getEndTime();
            if(hasTimeOverlap(slotStart, slotEnd, startTime, endTime))
                throw new IllegalStateException("You already have a booking at this time slot");
        });

        Booking booking = Booking.builder()
                .establishment(establishment)
                .serviceOffered(serviceOffered)
                .professional(professional)
                .client(authenticatedUser)
                .bookingDate(createBookingDTO.getBookingDate())
                .startTime(createBookingDTO.getStartTime())
                .endTime(slotEnd)
                .status(BookingStatus.CONFIRMED)
                .build();

        Booking saved = bookingRepository.save(booking);
        return toResponseDTO(saved);
    }

    public BookingResponseDTO cancel(UUID bookingId) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getClient().getId().equals(authenticatedUser.getId()))
            throw new AccessDeniedException("You cannot cancel this booking");

        if (!booking.getBookingDate().isAfter(LocalDate.now()))
            throw new IllegalStateException("Cancellation is only allowed until 23:59 of the day before the booking");

        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return toResponseDTO(saved);
    }

    private BookingResponseDTO toResponseDTO(Booking booking) {
        return new BookingResponseDTO(
            booking.getId(),
            new EstablishmentSummaryDTO(
                booking.getEstablishment().getId(),
                booking.getEstablishment().getName(),
                booking.getEstablishment().getCategory(),
                booking.getEstablishment().getPhone(),
                booking.getEstablishment().getCep(),
                booking.getEstablishment().getAddress(),
                booking.getEstablishment().getNumber(),
                booking.getEstablishment().getNeighborhood(),
                booking.getEstablishment().getCity(),
                booking.getEstablishment().getState(),
                booking.getEstablishment().getCreatedAt(),
                booking.getEstablishment().getUpdatedAt()
            ),
            new ServiceOfferedSummaryDTO(
                booking.getServiceOffered().getId(),
                booking.getServiceOffered().getEstablishment().getId(),
                booking.getServiceOffered().getName(),
                booking.getServiceOffered().getDescription(),
                booking.getServiceOffered().getDurationMinutes(),
                booking.getServiceOffered().getPrice(),
                booking.getServiceOffered().getCreatedAt(),
                booking.getServiceOffered().getUpdatedAt()
            ),
            new UserSummaryDTO(
                booking.getProfessional().getId(),
                booking.getProfessional().getName(),
                booking.getProfessional().getEmail()
            ),
            new UserSummaryDTO(
                booking.getClient().getId(),
                booking.getClient().getName(),
                booking.getClient().getEmail()
            ),
            booking.getBookingDate(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getStatus(),
            booking.getCreatedAt(),
            booking.getUpdatedAt()
        );
    }

    private boolean hasTimeOverlap(LocalTime slotStart, LocalTime slotEnd, LocalTime existingStart, LocalTime existingEnd) {
        return slotStart.isBefore(existingEnd) && slotEnd.isAfter(existingStart);
    }
}
