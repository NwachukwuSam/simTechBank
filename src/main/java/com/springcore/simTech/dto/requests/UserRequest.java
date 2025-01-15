package com.springcore.simTech.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String otherNames;
    private String email;
    private String phoneNumber;
    private String address;
    private String stateOfOrigin;
    private String alternatePhoneNumber;
    private String status;
}
