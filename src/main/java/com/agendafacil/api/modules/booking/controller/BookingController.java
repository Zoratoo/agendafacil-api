package com.agendafacil.api.modules.booking.controller;

import com.agendafacil.api.modules.booking.dto.AvailableSlotsDTO;
import com.agendafacil.api.modules.booking.dto.BookingResponseDTO;
import com.agendafacil.api.modules.booking.dto.CreateBookingDTO;
import com.agendafacil.api.modules.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/available-slots")
    @PreAuthorize("isAuthenticated()")
    public List<AvailableSlotsDTO> getAvailableSlots(
            @RequestParam UUID professionalId,
            @RequestParam UUID establishmentId,
            @RequestParam UUID serviceId,
            @RequestParam LocalDate date
    ) {
        return bookingService.getAvailableSlots(professionalId, establishmentId, serviceId, date);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public BookingResponseDTO create(@RequestBody CreateBookingDTO createBookingDTO) {
        return bookingService.create(createBookingDTO);
    }

    @PatchMapping("/{bookingId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public BookingResponseDTO cancel(@PathVariable UUID bookingId) {
        return bookingService.cancel(bookingId);
    }
}
