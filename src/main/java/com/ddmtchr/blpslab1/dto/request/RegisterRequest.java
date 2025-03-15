package com.ddmtchr.blpslab1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    private Long money;

    private Set<String> roles;
}
