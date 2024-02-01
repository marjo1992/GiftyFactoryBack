package com.marjo.giftyfactoryback.resource.input;

import java.time.LocalDate;

public record CreateOrModifyPersonRequest(
    String name,
    String firstname,
    LocalDate birthdate) {
}
