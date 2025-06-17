package com.springcore.simTech.services.userService;

import com.springcore.simTech.dto.requests.*;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.dto.response.LoginResponse;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameRequest(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

    BankResponse transfer(TransferRequest transferRequest);
    LoginResponse login(LoginRequest loginRequest);

}
