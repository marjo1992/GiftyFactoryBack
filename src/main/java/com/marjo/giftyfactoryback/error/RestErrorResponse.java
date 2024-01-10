package com.marjo.giftyfactoryback.error;

import java.time.LocalDateTime;
import java.util.Objects;

public record RestErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp) {

    public RestErrorResponse {
        Objects.requireNonNull(status);
        Objects.requireNonNull(message);
        Objects.requireNonNull(timestamp);
    }
}
