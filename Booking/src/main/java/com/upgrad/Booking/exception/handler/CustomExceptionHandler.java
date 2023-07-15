package com.upgrad.Booking.exception.handler;

import com.upgrad.Booking.dto.ErrorResponseDTO;
import com.upgrad.Booking.exception.BookingIdNotFoundException;
import com.upgrad.Booking.exception.InvalidPaymentMethodException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidPaymentMethodException.class)
    public ResponseEntity<ErrorResponseDTO> invalidPaymentTypeException(){
        ErrorResponseDTO response = new ErrorResponseDTO("Invalid mode of payment", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingIdNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> bookingIdNotFoundException(){

        ErrorResponseDTO response = new ErrorResponseDTO("Invalid Booking Id",HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}
