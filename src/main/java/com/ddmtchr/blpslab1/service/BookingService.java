package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.dto.producer.CheckDto;
import com.ddmtchr.blpslab1.dto.request.BookingChangesDto;
import com.ddmtchr.blpslab1.dto.request.BookingRequestDto;
import com.ddmtchr.blpslab1.dto.response.BookingResponseDto;
import com.ddmtchr.blpslab1.entity.Booking;
import com.ddmtchr.blpslab1.entity.BookingStatus;
import com.ddmtchr.blpslab1.entity.Estate;
import com.ddmtchr.blpslab1.exception.InconsistentRequestException;
import com.ddmtchr.blpslab1.exception.InsufficientFundsException;
import com.ddmtchr.blpslab1.exception.NoPermissionException;
import com.ddmtchr.blpslab1.exception.NotFoundException;
import com.ddmtchr.blpslab1.mapper.BookingMapper;
import com.ddmtchr.blpslab1.repository.BookingRepository;
import com.ddmtchr.blpslab1.repository.EstateRepository;
import com.ddmtchr.blpslab1.security.entity.User;
import com.ddmtchr.blpslab1.security.repository.UserRepository;
import com.ddmtchr.blpslab1.security.util.SecurityUtil;
import com.ddmtchr.blpslab1.service.producer.StompMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingMapper mapper = BookingMapper.INSTANCE;
    private final BookingRepository repository;
    private final EstateRepository estateRepository;
    private final UserRepository userRepository;
    private final CheckMessageService checkMessageService;

    @Transactional
    public BookingResponseDto addBooking(BookingRequestDto dto) {
        Booking entity = this.mapper.toEntity(dto);
        Estate estate = this.estateRepository.findById(dto.getEstateId()).orElseThrow(() -> new NotFoundException(String.format("Estate with id=%s was not found", dto.getEstateId())));
        User guest = this.userRepository.findById(dto.getGuestId()).orElseThrow(() -> new NotFoundException(String.format("User with id=%s was not found", dto.getGuestId())));

        Long amountToPay = estate.getPrice() * ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());
        entity.setAmount(amountToPay);
        entity.setEstate(estate);
        entity.setGuest(guest);
        entity.setStatus(BookingStatus.PENDING_HOST_REVIEW);

        return this.mapper.toResponseDto(this.repository.save(entity));
    }

    public List<BookingResponseDto> findAllByHost(String username) {
        return this.repository.findByEstateOwnerUsername(username).stream().map(this.mapper::toResponseDto).toList();
    }

    public List<BookingResponseDto> findAllByGuest(String username) {
        return this.repository.findByGuestUsername(username).stream().map(this.mapper::toResponseDto).toList();
    }

    public BookingResponseDto findById(Long id) {
        return this.repository.findById(id).map(this.mapper::toResponseDto).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));
    }

    @Transactional
    public BookingResponseDto approveById(Long id) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));

        checkHostPermission(booking);
        if (!booking.getStatus().equals(BookingStatus.PENDING_HOST_REVIEW)) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be approved", id));
        }

        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        booking.setPaymentRequestTime(LocalDateTime.now());
        return this.mapper.toResponseDto(this.repository.save(booking));
    }

    @Transactional
    public BookingResponseDto rejectById(Long id) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));

        checkHostPermission(booking);
        if (!booking.getStatus().equals(BookingStatus.PENDING_HOST_REVIEW)) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be rejected", id));
        }

        booking.setStatus(BookingStatus.REJECTED_BY_HOST);
        return this.mapper.toResponseDto(this.repository.save(booking));
    }

    @Transactional
    public BookingResponseDto changeById(Long id, BookingChangesDto dto) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));

        checkHostPermission(booking);
        if (!(booking.getStatus().equals(BookingStatus.PENDING_HOST_REVIEW) || booking.getStatus().equals(BookingStatus.PENDING_CHANGES_REVIEW))) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be changed", id));
        }

        this.mapper.updateBooking(dto, booking);
        return this.mapper.toResponseDto(this.repository.save(booking));
    }

    @Transactional
    public BookingResponseDto approveChangesById(Long id) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));

        checkGuestPermission(booking);
        if (!booking.getStatus().equals(BookingStatus.PENDING_CHANGES_REVIEW)) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be approved by guest", id));
        }

        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        booking.setPaymentRequestTime(LocalDateTime.now());
        return this.mapper.toResponseDto(this.repository.save(booking));
    }

    @Transactional
    public BookingResponseDto rejectChangesById(Long id) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));

        checkGuestPermission(booking);
        if (!booking.getStatus().equals(BookingStatus.PENDING_CHANGES_REVIEW)) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be rejected by guest", id));
        }

        booking.setStatus(BookingStatus.REJECTED_BY_GUEST);
        return this.mapper.toResponseDto(this.repository.save(booking));
    }

    @Transactional
    public void payForBooking(Long id) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));
        String username = SecurityUtil.getCurrentUser().getUsername();
        User guest = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format("User with username=%s was not found", username)));

        checkGuestPermission(booking);
        if (!booking.getStatus().equals(BookingStatus.PENDING_PAYMENT)) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be paid by guest", id));
        }

        Long amountToPay = booking.getAmount();
        if (guest.getMoney() < amountToPay) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        guest.setMoney(guest.getMoney() - amountToPay);
        booking.setStatus(BookingStatus.PENDING_CHECK_IN);
        this.repository.save(booking);
        this.userRepository.save(guest);

        this.checkMessageService.save(new CheckDto(
                booking.getId(),
                guest.getUsername(),
                booking.getEstate().getName(),
                amountToPay,
                guest.getEmail(),
                booking.getStartDate(),
                booking.getEndDate()));
    }

    @Transactional
    public BookingResponseDto checkInById(Long id) {
        Booking booking = this.repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Booking with id=%s was not found", id)));

        checkGuestPermission(booking);
        if (!booking.getStatus().equals(BookingStatus.PENDING_CHECK_IN)) {
            throw new InconsistentRequestException(String.format("Booking with id=%s cannot be checked in", id));
        }

        booking.setStatus(BookingStatus.PENDING_PAYMENT_TO_HOST);
        booking.setPayoutScheduledAt(LocalDateTime.now().plusMinutes(1));
        return this.mapper.toResponseDto(this.repository.save(booking));
    }


    private void checkHostPermission(Booking booking) {
        if (!booking.getEstate().getOwner().getUsername().equals(SecurityUtil.getCurrentUser().getUsername())) {
            throw new NoPermissionException(String.format("No permission to edit booking with id=%s", booking.getId()));
        }
    }

    private void checkGuestPermission(Booking booking) {
        if (!booking.getGuest().getUsername().equals(SecurityUtil.getCurrentUser().getUsername())) {
            throw new NoPermissionException(String.format("No permission to edit booking with id=%s", booking.getId()));
        }
    }
}
