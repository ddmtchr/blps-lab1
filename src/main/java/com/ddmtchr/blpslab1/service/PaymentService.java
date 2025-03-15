package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.security.entity.User;
import com.ddmtchr.blpslab1.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserRepository userRepository;

    @Transactional
    public void payToHost(User host, Long amount) {
        host.setMoney(host.getMoney() + amount);
        this.userRepository.save(host);
    }
}
