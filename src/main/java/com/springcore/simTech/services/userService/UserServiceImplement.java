package com.springcore.simTech.services.userService;

import com.springcore.simTech.data.model.User;
import com.springcore.simTech.dto.requests.UserRequest;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.utilities.AccountUtils;

import java.math.BigDecimal;

public class UserServiceImplement implements UserService {
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Save a new user and save to the db
         */
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .otherNames(userRequest.getOtherNames())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .address(userRequest.getAddress())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();
    }
}
