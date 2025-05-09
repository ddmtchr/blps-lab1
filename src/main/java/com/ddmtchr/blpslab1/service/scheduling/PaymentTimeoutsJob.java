package com.ddmtchr.blpslab1.service.scheduling;

import com.ddmtchr.blpslab1.service.PaymentTimeoutService;
import lombok.RequiredArgsConstructor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PaymentTimeoutsJob extends QuartzJobBean {

    private final PaymentTimeoutService paymentTimeoutService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.paymentTimeoutService.processPaymentTimeout();
    }
}
