package com.ddmtchr.blpslab1.service;

import com.ddmtchr.blpslab1.dto.request.EstateRequestDto;
import com.ddmtchr.blpslab1.dto.response.EstateResponseDto;
import com.ddmtchr.blpslab1.entity.Estate;
import com.ddmtchr.blpslab1.exception.NotFoundException;
import com.ddmtchr.blpslab1.mapper.EstateMapper;
import com.ddmtchr.blpslab1.repository.EstateRepository;
import com.ddmtchr.blpslab1.security.entity.User;
import com.ddmtchr.blpslab1.security.jwt.JwtUtils;
import com.ddmtchr.blpslab1.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstateService {
    private final EstateRepository estateRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final EstateMapper mapper = EstateMapper.INSTANCE;

    public EstateResponseDto addEstate(EstateRequestDto dto) {
        String username = jwtUtils.getCurrentUser().getUsername();
        User owner = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(String.format("User with username=%s was not found", username)));
        Estate entity = this.mapper.toEntity(dto);
        entity.setOwner(owner);
        return this.mapper.toResponseDto(this.estateRepository.save(entity));
    }

    public List<EstateResponseDto> findAll() {
        return this.estateRepository.findAll().stream().map(this.mapper::toResponseDto).toList();
    }

}
