package com.marjo.giftyfactoryback.resource.input;

import java.util.List;

public record CreateOrModifyIdeaRequest(
    String eventId,
    String name,
    String urlLink,
    Long price,
    List<Long> recipientsId) {
}
