package com.ddmtchr.blpslab1.validation;

import com.ddmtchr.blpslab1.dto.request.BookingRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, BookingRequestDto> {
    @Override
    public boolean isValid(BookingRequestDto value, ConstraintValidatorContext context) {
        if (value.getStartDate() == null || value.getEndDate() == null) {
            return true;
        }
        return !value.getStartDate().isAfter(value.getEndDate());
    }
}
