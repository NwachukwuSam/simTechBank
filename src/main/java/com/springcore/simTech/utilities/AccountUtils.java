package com.springcore.simTech.utilities;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User with this email already exists";
    public static final String ACCOUNT_CREATED_CODE = "002";
    public static final String ACCOUNT_CREATED_MESSAGE = "Account created successfully";


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
