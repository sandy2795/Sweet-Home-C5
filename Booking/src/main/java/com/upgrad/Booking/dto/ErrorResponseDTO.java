package com.upgrad.Booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponseDTO {

    @JsonProperty
    String message;
    @JsonProperty
    int statusCode;

    public ErrorResponseDTO(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}