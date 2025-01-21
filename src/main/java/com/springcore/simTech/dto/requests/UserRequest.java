package com.springcore.simTech.dto.requests;

import lombok.*;


@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String otherNames;
    private String email;
    private String phoneNumber;
    private String address;
    private String stateOfOrigin;
    private String dateOfBirth;
    private String alternatePhoneNumber;

}
