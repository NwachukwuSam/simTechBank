package com.springcore.simTech.services.userService;

import com.springcore.simTech.data.model.User;
import com.springcore.simTech.data.repository.UserRepository;
import com.springcore.simTech.dto.requests.UserRequest;
import com.springcore.simTech.dto.response.AccountInfo;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.utilities.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    final UserRepository userRepository;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .otherNames(userRequest.getOtherNames())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .dateOfBirth(userRequest.getDateOfBirth())
                .address(userRequest.getAddress())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName()+ " " + savedUser.getLastName()+ " "+ savedUser.getOtherNames())
                        .build())
                .build();
    }
}
