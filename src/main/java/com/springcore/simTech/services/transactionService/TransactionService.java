package com.springcore.simTech.services.transactionService;

import com.springcore.simTech.data.model.Transaction;
import com.springcore.simTech.dto.requests.TransactionRequest;

public interface TransactionService {
    void saveTransaction(TransactionRequest request);
}
