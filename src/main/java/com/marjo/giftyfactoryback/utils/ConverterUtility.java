package com.marjo.giftyfactoryback.utils;

import java.util.function.Function;

import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.resource.output.PersonResponse;
import com.marjo.giftyfactoryback.resource.output.UserResponse;

public class ConverterUtility {

        public static final Function<Person, PersonResponse> personToPersonResponse = p -> new PersonResponse(p.getId(),
                        p.getName(), p.getFirstname(), p.getBirthdate(),
                        p.getOwner() != null ? p.getOwner().getPersonId() : null);

        public static final Function<User, UserResponse> userToUserResponse = u -> new UserResponse(u.getPersonId(),
                        u.getUsername(), u.getEmail(), u.getPicture(), personToPersonResponse.apply(u.getPerson()));

}
