package com.springcore.simTech.services.userService;

import com.springcore.simTech.dto.requests.CreditDebitRequest;
import com.springcore.simTech.dto.requests.EnquiryRequest;
import com.springcore.simTech.dto.requests.UserRequest;
import com.springcore.simTech.dto.response.BankResponse;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameRequest(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);

}
