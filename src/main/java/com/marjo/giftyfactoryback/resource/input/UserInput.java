package com.marjo.giftyfactoryback.resource.input;

public record UserInput(
    String email,
    String password,
    String picture,
    boolean isHimselfOwner) {
}
