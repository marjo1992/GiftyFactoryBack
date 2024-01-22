package com.marjo.giftyfactoryback.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.error.exception.UserAlreadyExistsException;
import com.marjo.giftyfactoryback.repository.PersonRepository;
import com.marjo.giftyfactoryback.repository.UserRepository;
import com.marjo.giftyfactoryback.resource.input.NewPersonRequest;
import com.marjo.giftyfactoryback.resource.input.SignupRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

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
    public void createNewUser(SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.username())) {
            throw new UserAlreadyExistsException("A user with username " + signUpRequest.username() + " already exists");
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new UserAlreadyExistsException("A user with email " + signUpRequest.username() + " already exists");
        }

        Person person = new Person();
        person.setName(signUpRequest.name());
        person.setFirstname(signUpRequest.firstname());
        person.setBirthdate(signUpRequest.birthdate());

        personRepository.save(person);
        // TODO : ou Person createdPerson = personRepository.save(personToCreate);

        User user = new User();
        user.setPerson(person); // TODO : ou user.setPersonId(createdPerson.getId());        
        user.setUsername(signUpRequest.username());
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setPicture(signUpRequest.picture());
        user.setHimselfOwner(true);
        
        userRepository.save(user);
    }

    @Transactional
    public String createNewPersonWithAnExistingUser(Long personId, NewPersonRequest newPersonRequest) {

        User creatorUser = new User();
        creatorUser.setPersonId(personId);

        Person person = new Person();
        person.setFirstname(newPersonRequest.firstname());
        person.setName(newPersonRequest.name());
        person.setBirthdate(newPersonRequest.birthdate());
        person.setOwner(creatorUser);
        Person createdPerson = personRepository.save(person);

        return "person " + createdPerson.getName() + " créé  avec l'id " + createdPerson.getId();
    }
}