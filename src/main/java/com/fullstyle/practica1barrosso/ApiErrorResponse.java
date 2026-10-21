package com.fullstyle.practica1barrosso;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public record ApiErrorResponse(int status, String error, String message, Instant timestamp) {

    public static ApiErrorResponse of(HttpStatus status, String message) {
        return new ApiErrorResponse(status.value(), status.getReasonPhrase(), message, Instant.now());
    }
}
