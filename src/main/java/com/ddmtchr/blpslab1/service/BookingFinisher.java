package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.entity.BookingStatus;
import com.ddmtchr.blpslab1.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingFinisher {
    private final BookingRepository bookingRepository;

    @Transactional
    public void processFinish() {
        LocalDate now = LocalDate.now();

        this.bookingRepository.updateStatusByStatusAndEndDateBefore(BookingStatus.IN_PROGRESS, now, BookingStatus.FINISHED);
    }

}
