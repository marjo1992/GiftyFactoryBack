package com.marjo.giftyfactoryback;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.marjo.giftyfactoryback.entity.Person;
import com.marjo.giftyfactoryback.service.PersonService;

@RestController
@RequestMapping("/api/person")
public class PersonResource {

    @Autowired
    PersonService service;

    private final Consumer<List<String>> validateAtLeastOneNotBlank = elements -> elements.stream()
            .filter(StringUtils::isNotBlank).findAny()
            .orElseThrow(() -> new IllegalStateException("At least one params should be not blank"));

    @GetMapping("/searchexact")
    public List<Person> seachExactly(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "birthdate", required = false) LocalDate birthdate) {

        validateAtLeastOneNotBlank
                .accept(Arrays.asList(name, firstname, birthdate != null ? birthdate.toString() : null));

        return service.findBy(name, firstname, birthdate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createATestPerson() {
        return service.create();
    }

    @GetMapping
    public String test() {
        return "hello to person services";
    }

}
