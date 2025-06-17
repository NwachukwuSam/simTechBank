package com.springcore.simTech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String responseCode;
    private String responseMessage;
    private BigDecimal accountBalance;
    private String firstName;
    private String lastName;
    private String email;

}
