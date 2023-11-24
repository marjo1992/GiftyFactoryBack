package com.marjo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.marjo.giftyfactoryback.entity.Person;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PersonResourceIT {

    private static final String API_ROOT
      = "http://localhost:8081/api/person";

    private Person createRandomPerson() {
        Person person = new Person();
        person.setName(RandomStringUtils.randomAlphabetic(10));
        person.setFirstname(RandomStringUtils.randomAlphabetic(15));
        person.setBirthday(LocalDate.of(2020, 1, 8));
        return person;
    }

    private String createPersonAsUri(Person person) {
        Response response = RestAssured.given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(person)
          .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = RestAssured.get(API_ROOT);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetPersonsByTitle_thenOK() {
        Person person = createRandomPerson();
        createPersonAsUri(person);
        Response response = RestAssured.post(API_ROOT + "/person/");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class).size() > 0);
    }
}
