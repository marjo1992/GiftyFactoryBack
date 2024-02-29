package com.marjo.giftyfactoryback.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.marjo.giftyfactoryback.entity.Idea;
import com.marjo.giftyfactoryback.entity.IdeaStatusEnum;
import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.entity.VisibilityEnum;
import com.marjo.giftyfactoryback.error.exception.NoPersonExistsException;
import com.marjo.giftyfactoryback.repository.IdeaRepository;
import com.marjo.giftyfactoryback.repository.PersonRepository;
import com.marjo.giftyfactoryback.repository.UserRepository;
import com.marjo.giftyfactoryback.resource.input.CreateOrModifyIdeaRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    @Transactional
    public Optional<Idea> findById(final long id) {
        return ideaRepository.findById(id);
    }

    @Transactional
    public long createIdea(long userConnectedPersonId, CreateOrModifyIdeaRequest createIdeaRequest) {

        User connectedUser = userRepository.findById(userConnectedPersonId).get();

        if (CollectionUtils.isEmpty(createIdeaRequest.recipientsId()) 
            || createIdeaRequest.recipientsId().contains(null)) {
            throw new NoPersonExistsException("At least one recipient person should be given");
        }
        List<Person> recipients = createIdeaRequest.recipientsId().stream()
        .map(id -> personRepository.findById(id))
        .map(p -> p.orElseThrow(() -> new NoPersonExistsException("Recipient person doesn't exist")))
        .toList();

        Idea idea = new Idea();
        idea.setAuthor(connectedUser);
        idea.setName(createIdeaRequest.name());
        idea.setPrice(createIdeaRequest.price());
        idea.setRecipients(recipients);
        idea.setStatus(IdeaStatusEnum.DRAFT);
        idea.setUrlLink(createIdeaRequest.urlLink());
        idea.setVisibility(VisibilityEnum.PRIVATE);
        idea.setEvent(null);
        Idea createdIdea = ideaRepository.save(idea);

        return createdIdea.getId();
    }

}