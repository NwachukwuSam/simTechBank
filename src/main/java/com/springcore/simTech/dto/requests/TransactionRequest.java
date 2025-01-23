package com.springcore.simTech.dto.requests;


import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private String transactionType;
    private BigDecimal transactionAmount;
    private String accountNumber;
    private String status;

}
