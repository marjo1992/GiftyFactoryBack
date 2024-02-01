package com.marjo.giftyfactoryback.utils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.error.exception.NotAuthorizedActionException;

public class CheckUtility {

    public final static Consumer<List<String>> validateAtLeastOneNotBlank = elements -> elements.stream()
            .filter(StringUtils::isNotBlank).findAny()
            .orElseThrow(() -> new IllegalStateException("At least one params should be not blank"));

    public final static BiPredicate<Person, User> confirmOrThrowPersonIsAssociatedToOrOwnedByUser = (personToModify,
            userConnected) -> {
        if (userConnected.getPersonId() != personToModify.getId()
                && userConnected.getPersonId() != personToModify.getOwner().getPersonId()) {
            new NotAuthorizedActionException("User has no rights on this person");
        }
        return true;
    };

}
