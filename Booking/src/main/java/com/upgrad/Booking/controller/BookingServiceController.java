package com.upgrad.Booking.controller;

import com.upgrad.Booking.dto.BookingInfoEntityDTO;
import com.upgrad.Booking.dto.TransactionDetailsEntityDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.BookingIdNotFoundException;
import com.upgrad.Booking.exception.InvalidPaymentMethodException;
import com.upgrad.Booking.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel/")
public class BookingServiceController {

    BookingService bookingService;
    ModelMapper modelMapper;

    @Autowired
    public BookingServiceController(BookingService bookingService,ModelMapper modelMapper){
        this.bookingService=bookingService;
        this.modelMapper=modelMapper;
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingInfoEntityDTO> book(@RequestBody BookingInfoEntityDTO bookingInfoEntityDto){

        BookingInfoEntity bookingInfoEntity = modelMapper.map(bookingInfoEntityDto,BookingInfoEntity.class);
        BookingInfoEntity savedBookingEntity=bookingService.book(bookingInfoEntity);
        BookingInfoEntityDTO savedBookingEntityDto=modelMapper.map(savedBookingEntity, BookingInfoEntityDTO.class);

        return new ResponseEntity(savedBookingEntityDto, HttpStatus.CREATED);
    }


    @PostMapping("/booking/{id}/transaction")
    public ResponseEntity<BookingInfoEntityDTO> transaction(@PathVariable("id") int id, @RequestBody TransactionDetailsEntityDTO transactionDetailsEntityDto) throws InvalidPaymentMethodException, BookingIdNotFoundException {

        BookingInfoEntity bookingInfoEntity= bookingService.makeTransaction(transactionDetailsEntityDto,id);
        BookingInfoEntityDTO bookingInfoEntityDto=modelMapper.map(bookingInfoEntity, BookingInfoEntityDTO.class);
        return new ResponseEntity(bookingInfoEntityDto,HttpStatus.CREATED);
    }

}
