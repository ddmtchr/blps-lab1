package com.ddmtchr.blpslab1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String jwt;
    private Long id;
    private String username;
    private List<String> roles;
}
