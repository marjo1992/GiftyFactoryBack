package com.marjo.giftyfactoryback.resource.output;

import java.time.LocalDate;
import java.util.function.Function;

import com.marjo.giftyfactoryback.entity.Person;

public record PersonResponse(
        long id,
        String name,
        String firstname,
        LocalDate birthdate,
        Long ownerId) {

    public static final Function<Person, PersonResponse> fromPerson = p -> new PersonResponse(p.getId(),
            p.getName(), p.getFirstname(), p.getBirthdate(),
            p.getOwner() != null ? p.getOwner().getPersonId() : null);
}
