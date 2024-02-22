package com.marjo.giftyfactoryback.resource.output;

import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marjo.giftyfactoryback.entity.User;

public record UserResponse(
        long personId,
        String username,
        String email,
        String picture,
        @JsonProperty("person") PersonResponse personResponse) {

    public static final Function<User, UserResponse> fromUser = u -> new UserResponse(u.getPersonId(),
            u.getUsername(), u.getEmail(), u.getPicture(), PersonResponse.fromPerson.apply(u.getPerson()));
}
