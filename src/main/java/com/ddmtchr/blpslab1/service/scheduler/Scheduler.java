package com.ddmtchr.blpslab1.service.scheduler;

import com.ddmtchr.blpslab1.service.BookingFinisher;
import com.ddmtchr.blpslab1.service.PaymentTimeoutService;
import com.ddmtchr.blpslab1.service.PayoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Scheduler { // для проблемы с Scheduled и Transactional
    private static final long PAYMENT_TIMEOUT_CHECK_RATE_MS = 60 * 1000;
    private static final long PAYOUT_CHECK_RATE_MS = 20 * 1000;
    private static final long BOOKING_FINISH_CHECK_RATE_MS = 10 * 60 * 1000;

    private final PaymentTimeoutService paymentTimeoutService;
    private final PayoutService payoutService;
    private final BookingFinisher bookingFinisher;

    @Scheduled(fixedRate = PAYMENT_TIMEOUT_CHECK_RATE_MS)
    public void checkPaymentTimeouts() {
        this.paymentTimeoutService.processPaymentTimeout();
    }

    @Scheduled(fixedRate = PAYOUT_CHECK_RATE_MS)
    public void checkPayouts() {
        this.payoutService.processPayouts();
    }

    @Scheduled(fixedRate = BOOKING_FINISH_CHECK_RATE_MS)
    public void checkBookingFinishes() {
        this.bookingFinisher.processFinish();
    }
}
