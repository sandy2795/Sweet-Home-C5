package com.upgrad.Booking.services;

import com.upgrad.Booking.dto.TransactionDetailsEntityDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.BookingIdNotFoundException;
import com.upgrad.Booking.exception.InvalidPaymentMethodException;

import java.util.List;


public interface BookingService {

    public BookingInfoEntity book(BookingInfoEntity bookingInfoEntity);
    public BookingInfoEntity getBookingBasedOnId(int id);
    public List<BookingInfoEntity> getAllBookings();
    public void deleteBookingID(BookingInfoEntity bookingInfoEntity);
    public BookingInfoEntity updateBooking(BookingInfoEntity bookingInfoEntity);
    public BookingInfoEntity makeTransaction(TransactionDetailsEntityDTO transactionDetailsEntityDto, int id) throws InvalidPaymentMethodException, BookingIdNotFoundException;

}
