package com.springcore.simTech.services.transactionService;

import com.springcore.simTech.data.model.Transaction;
import com.springcore.simTech.data.repository.TransactionRepository;
import com.springcore.simTech.dto.requests.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class TransactionServiceImplement implements TransactionService {

    final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .transactionAmount(request.getTransactionAmount())
                .accountNumber(String.valueOf(request.getAccountNumber()))
                .status("SUCCESS")
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction Saved");
    }
}
