package com.springcore.simTech.utilities;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User with this email already exists";
    public static final String ACCOUNT_CREATED_CODE = "002";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account created successfully";
    public static final String ACCOUNT_DOES_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "User with Account details does not exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_SUCCESSFULLY_FOUND_MESSAGE = "Account Successfully found";
    public static final String ACCOUNT_CREDITED_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE = "Account has been Credited successfully";
    public static final String ACCOUNT_DEBITED_CODE = "006";
    public static final String ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE = "Account has been Debited successfully";
    public static final String INSUFFICIENT_FUNDS_CODE = "007";
    public static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient Funds";
    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful";

    public static String generateAccountNumber() {
        /**
         * Account Number is Current Year plus 6 random numbers
         */
        Year currentYear = Year.now();
        int maxValue = 999999;
        int minValue = 100000;

        int randomNumber = (int)Math.floor(Math.random()*(maxValue - minValue +1)- minValue);

        String randomString = String.valueOf(randomNumber);
        String year = String.valueOf(currentYear);

        StringBuilder accountNumber = new StringBuilder();

        return String.valueOf(accountNumber.append(year).append(randomString));
    }
}
