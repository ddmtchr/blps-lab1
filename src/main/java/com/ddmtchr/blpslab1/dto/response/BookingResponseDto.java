package com.ddmtchr.blpslab1.dto.response;

import com.ddmtchr.blpslab1.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private Long guestId;
    private Long estateId;
    private BookingStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long amount;
    private LocalDateTime paymentRequestDate;
    private LocalDateTime payoutScheduledAt;
}
