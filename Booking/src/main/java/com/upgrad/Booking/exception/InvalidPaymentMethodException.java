package com.upgrad.Booking.exception;

public class InvalidPaymentMethodException extends Throwable {
    public InvalidPaymentMethodException(String message) {
        super(message);
    }
}