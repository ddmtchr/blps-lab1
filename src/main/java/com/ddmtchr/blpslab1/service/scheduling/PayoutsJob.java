package com.ddmtchr.blpslab1.service.scheduling;

import com.ddmtchr.blpslab1.service.PayoutService;
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
public class PayoutsJob extends QuartzJobBean {

    private final PayoutService payoutService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        this.payoutService.processPayouts();
    }
}
