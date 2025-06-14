package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.dto.producer.CheckDto;
import com.ddmtchr.blpslab1.mapper.CheckMessageMapper;
import com.ddmtchr.blpslab1.repository.CheckMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckMessageService {

    private final CheckMessageMapper mapper = CheckMessageMapper.INSTANCE;
    private final CheckMessageRepository checkMessageRepository;

    @Transactional
    public CheckDto save(CheckDto dto) {
        return mapper.toDto(checkMessageRepository.save(mapper.toEntity(dto)));
    }

}
