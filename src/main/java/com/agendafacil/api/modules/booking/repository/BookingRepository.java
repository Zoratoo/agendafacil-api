package com.agendafacil.api.modules.booking.repository;

import com.agendafacil.api.modules.booking.entity.Booking;
import com.agendafacil.api.modules.booking.entity.BookingStatus;
import com.agendafacil.api.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByProfessionalAndBookingDateAndStatus(
        User professional,
        LocalDate bookingDate,
        BookingStatus status
    );

    List<Booking> findByClientAndBookingDateAndStatus(
        User client,
        LocalDate bookingDate,
        BookingStatus status
    );

    List<Booking> findByClient(User client);

    List<Booking> findByProfessionalAndBookingDate(User professional, LocalDate bookingDate);
}
