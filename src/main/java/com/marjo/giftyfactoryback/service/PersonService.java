package com.marjo.giftyfactoryback.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.error.exception.NoPersonExistsException;
import com.marjo.giftyfactoryback.error.exception.UserAlreadyExistsException;
import com.marjo.giftyfactoryback.repository.PersonRepository;
import com.marjo.giftyfactoryback.repository.UserRepository;
import com.marjo.giftyfactoryback.resource.input.CreateOrModifyPersonRequest;
import com.marjo.giftyfactoryback.resource.input.SignupRequest;
import com.marjo.giftyfactoryback.utils.CheckUtility;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public String greeting(String name) {
        return "hello " + name;
    }

    @Transactional
    public List<Person> findBy(String name, String firstname, LocalDate birthdate) {
        return personRepository.findBy(name, firstname, birthdate);
    }

    @Transactional
    public Optional<Person> findById(final long id) {
        return personRepository.findById(id);
    }

    @Transactional
    public void createNewUser(SignupRequest signUpRequest) throws MessagingException {

        if (userRepository.existsByUsername(signUpRequest.username())) {
            throw new UserAlreadyExistsException(
                    "A user with username " + signUpRequest.username() + " already exists");
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new UserAlreadyExistsException("A user with email " + signUpRequest.email() + " already exists");
        }

        Person person = new Person();
        person.setName(signUpRequest.name());
        person.setFirstname(signUpRequest.firstname());
        person.setBirthdate(signUpRequest.birthdate());

        personRepository.save(person);
        // TODO : ou Person createdPerson = personRepository.save(personToCreate);

        String token = UUID.randomUUID().toString().toUpperCase();

        User user = new User();
        user.setPerson(person); // TODO : ou user.setPersonId(createdPerson.getId());
        user.setUsername(signUpRequest.username());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setPicture(signUpRequest.picture());
        user.setHimselfOwner(true);
        user.setEmailToken(token);
        userRepository.save(user);

        emailService.sendHtmlEmail(user.getPersonId(), signUpRequest.email(), token);
    }

    @Transactional
    public long createNewPersonWithAnExistingUser(long personId, CreateOrModifyPersonRequest newPersonRequest) {

        User creatorUser = userRepository.findById(personId).get();

        Person person = new Person();
        person.setFirstname(newPersonRequest.firstname());
        person.setName(newPersonRequest.name());
        person.setBirthdate(newPersonRequest.birthdate());
        person.setOwner(creatorUser);

        Person createdPerson = personRepository.save(person);
        return createdPerson.getId();
    }

    @Transactional
    public void modifyPerson(long connectedPersonId, long personToModifyId,
            CreateOrModifyPersonRequest newPersonRequest) {

        User userConnected = getUserAssociatedToPerson(connectedPersonId);

        Person personToModify = findById(personToModifyId)
                .filter(p -> CheckUtility.confirmOrThrowPersonIsAssociatedToOrOwnedByUser.test(p, userConnected))
                .orElseThrow(() -> new NoPersonExistsException("Person to modify not exist"));

        personToModify.setFirstname(newPersonRequest.firstname());
        personToModify.setName(newPersonRequest.name());
        personToModify.setBirthdate(newPersonRequest.birthdate());

        personRepository.save(personToModify);
    }

    @Transactional
    public User getUserAssociatedToPerson(long personId) {
        return userRepository.findById(personId).orElseThrow(() -> new NoPersonExistsException("User not exist"));
    }

    @Transactional
    public List<Person> getPersonsOwnedByUser(long id) {
        return personRepository.findByOwner_PersonId(id);
    }

    @Transactional
    public void confirmEmail(String token, long id) {

        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            throw new NoPersonExistsException("Any person exist with id " + id);
        }

        User user = userOpt.get();

        if (BooleanUtils.isTrue(user.isEmailConfirmed())) {
            throw new NoPersonExistsException("This person has already confirmed his email");
        }

        if (!StringUtils.equals(user.getEmailToken(), token)) {
            throw new NoPersonExistsException("Invalid token");
        }

        user.setEmailConfirmed(true);
        user.setEmailToken(null);
        userRepository.save(user);
    }

}