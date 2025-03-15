package com.ddmtchr.blpslab1.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstateRequestDto {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long price;
}
