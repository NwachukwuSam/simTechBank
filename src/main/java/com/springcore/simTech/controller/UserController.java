package com.springcore.simTech.controller;

import com.springcore.simTech.dto.requests.CreditDebitRequest;
import com.springcore.simTech.dto.requests.EnquiryRequest;
import com.springcore.simTech.dto.requests.TransferRequest;
import com.springcore.simTech.dto.requests.UserRequest;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.services.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    final UserService userService;

    @PostMapping("/create-account")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/balance-enquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameRequest(request);
    }

    @PostMapping("/credit-account")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }
    @PostMapping("/debit-account")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }

}
