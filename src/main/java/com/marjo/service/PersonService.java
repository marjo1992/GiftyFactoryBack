package com.marjo.service;

import java.time.LocalDate;
import java.util.List;

import com.marjo.entity.Person;
import com.marjo.entity.User;
import com.marjo.repository.PersonRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PersonService {

    public String greeting(String name) {
        return "hello " + name;
    }

    @Inject
    EntityManager em;

    @Inject
    PersonRepository repository;

    @Transactional
    public List<Person> search(String name, String firstname, LocalDate birthday) {
        return repository.findBy(name, firstname, birthday);
    }

    @Transactional
    public String create() {
        Person person = new Person();
        person.setFirstname("Jean");
        person.setName("Toto");
        person.setBirthday(LocalDate.of(2017, 11, 15));
        em.persist(person);

        User user = new User();
        user.setPerson(person);
        user.setMail("test@mail.com");
        user.setPassword("password");
        em.persist(user);

        return "user " + person.getName() + " créé  avec l'id " + person.getId();
    }

}