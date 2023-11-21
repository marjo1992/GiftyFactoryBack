package com.marjo;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.marjo.entity.Person;
import com.marjo.service.PersonService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/person")
public class PersonResource {
    @Inject
    PersonService service;

    private final Consumer<List<String>> validateAtLeastOneNotBlank = elements -> elements.stream()
            .filter(StringUtils::isBlank).findAny()
            .orElseThrow(() -> new IllegalStateException("At least one params should be not blank"));

    @GET
    @Path("/searchexact")
    public List<Person> seachExactly(
            @QueryParam("name") String name,
            @QueryParam("firstname") String firstname,
            @QueryParam("birthdate") LocalDate birthdate) {

        validateAtLeastOneNotBlank.accept(List.of(name, firstname, birthdate != null ? birthdate.toString() : null));

        return service.search(name, firstname, birthdate);
    }

    @GET
    @Path("/create")
    public String createATestPerson() {
        return service.create();
    }

}
