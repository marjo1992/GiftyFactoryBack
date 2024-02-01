package com.marjo.giftyfactoryback.resource.output;

import java.time.LocalDate;

public record PersonResponse(
    long id,
    String name,
    String firstname,
    LocalDate birthdate,
    Long ownerId
    ) {
}

