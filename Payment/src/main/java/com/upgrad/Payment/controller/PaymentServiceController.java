package com.upgrad.Payment.controller;


import com.upgrad.Payment.dto.TransactionDetailsEntityDTO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import com.upgrad.Payment.services.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentServiceController {

    ModelMapper modelMapper;
    PaymentService paymentService;

    @Autowired
    public PaymentServiceController(ModelMapper modelMapper,PaymentService paymentService){
        this.modelMapper=modelMapper;
        this.paymentService=paymentService;
    }

    @PostMapping(value = "/transaction")
    public ResponseEntity<TransactionDetailsEntityDTO> makeTransaction(@RequestBody TransactionDetailsEntityDTO transactionDetailsEntityDTO){

        TransactionDetailsEntity transactionDetailsEntity =modelMapper.map(transactionDetailsEntityDTO,TransactionDetailsEntity.class);
        TransactionDetailsEntity savedTransactionDetailsEntity=paymentService.initiateTransaction(transactionDetailsEntity);
        TransactionDetailsEntityDTO savedTransactionDetailsEntityDTO =modelMapper.map(savedTransactionDetailsEntity, TransactionDetailsEntityDTO.class);

        return new ResponseEntity(savedTransactionDetailsEntityDTO.getTransactionId(), HttpStatus.CREATED) ;
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionDetailsEntityDTO> getTransactionDetailsById(@PathVariable("id") int transactionId){

        TransactionDetailsEntity transactionDetailsEntity=paymentService.getTransaction(transactionId);
        TransactionDetailsEntityDTO transactionDetailsEntityDTO=modelMapper.map(transactionDetailsEntity, TransactionDetailsEntityDTO.class);

        return new ResponseEntity(transactionDetailsEntityDTO,HttpStatus.OK);
    }
}
