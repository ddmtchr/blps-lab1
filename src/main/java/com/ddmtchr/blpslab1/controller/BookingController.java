package com.ddmtchr.blpslab1.controller;

import com.ddmtchr.blpslab1.dto.request.BookingChangesDto;
import com.ddmtchr.blpslab1.dto.request.BookingRequestDto;
import com.ddmtchr.blpslab1.dto.response.BookingResponseDto;
import com.ddmtchr.blpslab1.security.jwt.JwtUtils;
import com.ddmtchr.blpslab1.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final JwtUtils jwtUtils;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return new ResponseEntity<>(this.bookingService.addBooking(bookingRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("byHost")
    public ResponseEntity<List<BookingResponseDto>> findAllBookingsOfHost() {
        String username = this.jwtUtils.getCurrentUser().getUsername();
        return new ResponseEntity<>(this.bookingService.findAllByHost(username), HttpStatus.OK);
    }

    @GetMapping("byGuest")
    public ResponseEntity<List<BookingResponseDto>> findAllBookingsOfGuest() {
        String username = this.jwtUtils.getCurrentUser().getUsername();
        return new ResponseEntity<>(this.bookingService.findAllByGuest(username), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> findBookingById(@PathVariable Long id) {
        return new ResponseEntity<>(this.bookingService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<BookingResponseDto> approveBookingById(@PathVariable Long id) {
        return new ResponseEntity<>(this.bookingService.approveById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<BookingResponseDto> rejectBookingById(@PathVariable Long id) {
        return new ResponseEntity<>(this.bookingService.rejectById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/suggestChanges")
    public ResponseEntity<BookingResponseDto> suggestBookingChangesById(@PathVariable Long id, @RequestBody @Valid BookingChangesDto dto) {
        return new ResponseEntity<>(this.bookingService.changeById(id, dto), HttpStatus.OK);
    }

    @PutMapping("/{id}/approveChanges")
    public ResponseEntity<BookingResponseDto> approveBookingChangesById(@PathVariable Long id) {
        return new ResponseEntity<>(this.bookingService.approveChangesById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/rejectChanges")
    public ResponseEntity<BookingResponseDto> rejectBookingChangesById(@PathVariable Long id) {
        return new ResponseEntity<>(this.bookingService.rejectChangesById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/checkIn")
    public ResponseEntity<BookingResponseDto> checkInById(@PathVariable Long id) {
        return new ResponseEntity<>(this.bookingService.checkInById(id), HttpStatus.OK);
    }
}
