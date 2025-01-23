package com.springcore.simTech.services.userService;

import com.springcore.simTech.data.model.User;
import com.springcore.simTech.data.repository.UserRepository;
import com.springcore.simTech.dto.requests.*;
import com.springcore.simTech.dto.response.AccountInfo;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.services.emailService.EmailService;
import com.springcore.simTech.utilities.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {


    final UserRepository userRepository;

    final EmailService emailService;


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

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your account has been Successfully created! \n Your account details are: \n"+
                       "Account Name:"+ savedUser.getFirstName() +" "+ savedUser.getLastName()+ " " +savedUser.getOtherNames()+ "\n Account Number:"+ savedUser.getAccountNumber())
                .build();
        emailService.sendEmail(emailRequest);
        System.out.println("Email Sent");
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


    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean accountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!accountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_SUCCESSFULLY_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName()+" "+ foundUser.getLastName()+ " "+ foundUser.getOtherNames())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public String nameRequest(EnquiryRequest enquiryRequest) {
        boolean accountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!accountExists) {
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " "+ foundUser.getOtherNames();

    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        boolean accountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!accountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" " +userToCredit.getOtherNames())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        boolean accountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if(!accountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigDecimal availableBalance = userToDebit.getAccountBalance();
        BigDecimal debitAmount = creditDebitRequest.getAmount();
        if (availableBalance.compareTo(debitAmount) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();

        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(userToDebit);
        }

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" " +userToDebit.getOtherNames())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        boolean destinationAccountExists = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if(!destinationAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccount = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if(transferRequest.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccount);
        EmailRequest debitAlert = EmailRequest.builder()
                .recipient(sourceAccount.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("Hi "+sourceAccount.getFirstName()+" "+sourceAccount.getLastName()+ "\n "+ "The sum of"+transferRequest.getAmount() +"has been deducted from your account and \n transferred to"+ transferRequest.getDestinationAccountNumber()
                +"\n Your account balance is now "+ sourceAccount.getAccountBalance())
                .build();
        emailService.sendEmail(debitAlert);

        User destinationAccount = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationAccount);
        EmailRequest creditAlert = EmailRequest.builder()
                .recipient(destinationAccount.getEmail())
                .subject("CREDIT ALERT")
                .messageBody("Hi "+destinationAccount.getFirstName()+" "+destinationAccount.getLastName()+ "\n "+ "The sum of"+transferRequest.getAmount() +"has been sent to your account \n from "+ transferRequest.getSourceAccountNumber()
                        +"\n Your account balance is now "+ destinationAccount.getAccountBalance())
                .build();
        emailService.sendEmail(creditAlert);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
