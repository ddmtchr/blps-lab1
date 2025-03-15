package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.entity.Booking;
import com.ddmtchr.blpslab1.entity.BookingStatus;
import com.ddmtchr.blpslab1.repository.BookingRepository;
import com.ddmtchr.blpslab1.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PayoutService {

    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;

    @Transactional
    public void processPayouts() {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookingsForPayout = this.bookingRepository.findByStatusAndPayoutScheduledAtBefore(BookingStatus.PENDING_PAYMENT_TO_HOST, now);
        Set<Long> paidBookingIds = new HashSet<>();
        Map<User, Long> paymentsByHost = new HashMap<>();

        bookingsForPayout.forEach(b -> {
            User host = b.getEstate().getOwner();
            Long amount = b.getAmount();

            paymentsByHost.put(host, paymentsByHost.getOrDefault(host, 0L) + amount);
            paidBookingIds.add(b.getId());
        });

        paymentsByHost.forEach(this.paymentService::payToHost);

        this.bookingRepository.updateStatusByIdsAndPayoutScheduledAtBefore(paidBookingIds, now, BookingStatus.IN_PROGRESS);
    }
}
