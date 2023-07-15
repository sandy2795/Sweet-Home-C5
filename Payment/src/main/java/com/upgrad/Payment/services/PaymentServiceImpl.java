package com.upgrad.Payment.services;

import com.upgrad.Payment.dao.TransactionDetailsEntityDAO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    TransactionDetailsEntityDAO transactionDetailsEntityDAO;

    @Autowired
    public PaymentServiceImpl(TransactionDetailsEntityDAO transactionDetailsEntityDAO){
        this.transactionDetailsEntityDAO = transactionDetailsEntityDAO;
    }

    @Override
    public TransactionDetailsEntity initiateTransaction(TransactionDetailsEntity transactionDetailsEntity) {
        return transactionDetailsEntityDAO.save(transactionDetailsEntity);
    }

    @Override
    public TransactionDetailsEntity getTransaction(int id) {
        return transactionDetailsEntityDAO.getById(id);
    }
}

