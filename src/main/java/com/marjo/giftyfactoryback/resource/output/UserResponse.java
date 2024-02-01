package com.marjo.giftyfactoryback.resource.output;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
        long personId,
        String username,
        String email,
        String picture,
        @JsonProperty("person") PersonResponse personResponse) {
}
