package com.springcore.simTech.controller;

import com.springcore.simTech.dto.requests.UserRequest;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.services.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    final UserService userService;

    @PostMapping("/create-account")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

}
