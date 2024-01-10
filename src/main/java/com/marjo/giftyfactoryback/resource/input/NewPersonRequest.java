package com.marjo.giftyfactoryback.resource.input;

import java.time.LocalDate;

public record NewPersonRequest(
    String name,
    String firstname,
    LocalDate birthday) {
}
