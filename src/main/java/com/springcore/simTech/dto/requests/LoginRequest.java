package com.springcore.simTech.dto.requests;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
