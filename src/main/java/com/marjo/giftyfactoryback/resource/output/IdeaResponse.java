package com.marjo.giftyfactoryback.resource.output;

import java.util.List;
import java.util.function.Function;

import com.marjo.giftyfactoryback.entity.Idea;
import com.marjo.giftyfactoryback.entity.Person;

public record IdeaResponse(
        long id,
        String eventId,
        String name,
        String urlLink,
        Long price,
        String visibility,
        Long authorId,
        List<Long> recipientsId) {

            
        public static final Function<Idea, IdeaResponse> fromIdea = p -> new IdeaResponse(
                        p.getId(),
                        null,
                        p.getName(), 
                        p.getUrlLink(),
                        p.getPrice(),
                        p.getVisibility().code,
                        p.getAuthor().getPersonId(),
                        p.getRecipients().stream().map(Person::getId).toList());
}
