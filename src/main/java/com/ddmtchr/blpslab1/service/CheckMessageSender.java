package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.entity.CheckMessage;
import com.ddmtchr.blpslab1.mapper.CheckMessageMapper;
import com.ddmtchr.blpslab1.repository.CheckMessageRepository;
import com.ddmtchr.blpslab1.service.producer.StompMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CheckMessageSender {

    private final StompMessageSender messageSender;
    private final CheckMessageRepository checkMessageRepository;
    private final CheckMessageMapper mapper = CheckMessageMapper.INSTANCE;

    @Transactional
    public void processCheckMessages() {
        List<CheckMessage> messagedToSend = checkMessageRepository.findAllBySent(false);
        Set<Long> idsSent = new HashSet<>();

        messagedToSend.forEach(msg -> {
            this.messageSender.sendMessage(mapper.toDto(msg));
            idsSent.add(msg.getBookingId());
        });

        checkMessageRepository.setSentByBookingIdIn(idsSent);
    }
}
