package com.springcore.simTech.services.userService;

import com.springcore.simTech.data.model.Roles;
import com.springcore.simTech.data.model.Users;
import com.springcore.simTech.data.repository.UserRepository;
import com.springcore.simTech.dto.requests.*;
import com.springcore.simTech.dto.response.AccountInfo;
import com.springcore.simTech.dto.response.BankResponse;
import com.springcore.simTech.dto.response.LoginResponse;
import com.springcore.simTech.securities.JwtTokenProvider;
import com.springcore.simTech.services.emailService.EmailService;
import com.springcore.simTech.services.transactionService.TransactionService;
import com.springcore.simTech.utilities.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {


    final UserRepository userRepository;
    final EmailService emailService;
    final TransactionService transactionService;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final JwtTokenProvider jwtTokenProvider;



    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Users newUsers = Users.builder()
                .role(Roles.ROLE_USER)
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
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

        Users savedUsers = userRepository.save(newUsers);

        EmailRequest emailRequest = EmailRequest.builder()
                .recipient(savedUsers.getEmail())
                .subject("ACCOUNT CREATION")
                .recipient(savedUsers.getEmail())
                .messageBody("Congratulations! Your account has been Successfully created! \n Your account details are: \n"+
                       "Account Name:"+ savedUsers.getFirstName() +" "+ savedUsers.getLastName()+ " " + savedUsers.getOtherNames()+ "\n Account Number:"+ savedUsers.getAccountNumber())
                .build();
        emailService.sendEmail(emailRequest);
        System.out.println("Email Sent");
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUsers.getAccountNumber())
                        .accountBalance(savedUsers.getAccountBalance())
                        .accountName(savedUsers.getFirstName()+ " " + savedUsers.getLastName()+ " "+ savedUsers.getOtherNames())
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
        Users foundUsers = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_SUCCESSFULLY_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUsers.getAccountBalance())
                        .accountName(foundUsers.getFirstName()+" "+ foundUsers.getLastName()+ " "+ foundUsers.getOtherNames())
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

        Users foundUsers = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUsers.getFirstName() + " " + foundUsers.getLastName() + " "+ foundUsers.getOtherNames();

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
        Users usersToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        usersToCredit.setAccountBalance(usersToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(usersToCredit);

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType("CREDIT")
                .transactionAmount(creditDebitRequest.getAmount())
                .accountNumber(usersToCredit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(usersToCredit.getFirstName()+" "+ usersToCredit.getLastName()+" " + usersToCredit.getOtherNames())
                        .accountBalance(usersToCredit.getAccountBalance())
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
        Users usersToDebit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigDecimal availableBalance = usersToDebit.getAccountBalance();
        BigDecimal debitAmount = creditDebitRequest.getAmount();
        if (availableBalance.compareTo(debitAmount) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .accountInfo(null)
                    .build();

        } else {
            usersToDebit.setAccountBalance(usersToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
            userRepository.save(usersToDebit);
        }
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType("DEBIT")
                .transactionAmount(creditDebitRequest.getAmount())
                .accountNumber(usersToDebit.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(usersToDebit.getFirstName()+" "+ usersToDebit.getLastName()+" " + usersToDebit.getOtherNames())
                        .accountBalance(usersToDebit.getAccountBalance())
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
        Users sourceAccount = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
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

        Users destinationAccount = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationAccount);
        EmailRequest creditAlert = EmailRequest.builder()
                .recipient(destinationAccount.getEmail())
                .subject("CREDIT ALERT")
                .messageBody("Hi "+destinationAccount.getFirstName()+" "+destinationAccount.getLastName()+ "\n "+ "The sum of"+transferRequest.getAmount() +"has been sent to your account \n from "+ transferRequest.getSourceAccountNumber()
                        +"\n Your account balance is now "+ destinationAccount.getAccountBalance())
                .build();
        emailService.sendEmail(creditAlert);

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionType("CREDIT")
                .transactionAmount(transferRequest.getAmount())
                .accountNumber(destinationAccount.getAccountNumber())
                .build();
        transactionService.saveTransaction(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        System.out.println("Login request Started");
            Authentication authentication = null;
            authentication = authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        System.out.println("Login request received");
            String token = jwtTokenProvider.generateToken(authentication);

            Users users = userRepository.findUserByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("Login Successful");
            return LoginResponse.builder()
                    .responseCode("Login Successful")
                    .responseMessage(token)
                    .id(users.getId())
                    .firstName(users.getFirstName())
                    .lastName(users.getLastName())
                    .email(users.getEmail())
                    //.profilePicture(user.getProfilePicture())
                    .build();

    }
}
