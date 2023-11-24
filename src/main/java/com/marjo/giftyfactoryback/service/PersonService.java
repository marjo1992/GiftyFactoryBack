package com.marjo.giftyfactoryback.service;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.entity.User;
import com.marjo.giftyfactoryback.repository.PersonRepository;
import com.marjo.giftyfactoryback.repository.UserRepository;

import jakarta.transaction.Transactional;

@Controller
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    UserRepository userRepository;

    public String greeting(String name) {
        return "hello " + name;
    }

    @Transactional
    public List<Person> findBy(String name, String firstname, LocalDate birthday) {
        return personRepository.findBy(name, firstname, birthday);
    }

    @Transactional
    public String create() {
   
        final String randomName = RandomStringUtils.randomAlphabetic(10);

        Person person = new Person();
        person.setFirstname(randomName);
        person.setName(randomName);
        person.setBirthday(LocalDate.of(2017, 11, 15));
        personRepository.save(person);

        User user = new User();
        user.setPerson(person);
        user.setMail(randomName + "@mail.com");
        user.setPassword("password");
        userRepository.save(user);

        return "user " + person.getName() + " créé  avec l'id " + person.getId();
    }

}