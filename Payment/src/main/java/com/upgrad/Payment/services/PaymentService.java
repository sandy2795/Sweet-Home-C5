package com.upgrad.Payment.services;

import com.upgrad.Payment.entities.TransactionDetailsEntity;

public interface PaymentService {

    public TransactionDetailsEntity initiateTransaction(TransactionDetailsEntity transactionDetailsEntity);
    public TransactionDetailsEntity getTransaction(int id);
}
