package com.marjo.giftyfactoryback.resource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.marjo.giftyfactoryback.auth.UserDetailsImpl;
import com.marjo.giftyfactoryback.error.exception.NoResultException;
import com.marjo.giftyfactoryback.resource.input.CreateOrModifyPersonRequest;
import com.marjo.giftyfactoryback.resource.output.PersonResponse;
import com.marjo.giftyfactoryback.service.PersonService;
import com.marjo.giftyfactoryback.utils.CheckUtility;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/person")
public class PersonResource {

    @Autowired
    PersonService service;

    /******** Get person by id ********/
    @Operation(summary = "Get a person by id", description = "Returns a person as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No person exists with id xxx")
    })
    @GetMapping("/{id}")
    public PersonResponse getPerson(
            @PathVariable("id") @Parameter(name = "id", description = "Person id", example = "1") long id) {
        return service.findById(id)
                .map(PersonResponse.fromPerson)
                .orElseThrow(() -> new NoResultException(
                        "No person exists with id -'" + id));
    }

    /******** Get person by name, firstname or birthdate ********/
    @Operation(summary = "Get persons by name, surname or birthdate", description = "Returns persons as per name, surname or birthdate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "name, surname, or birthdate shoud be filled")
    })
    @GetMapping("/search")
    public List<PersonResponse> seachExactly(
            @Parameter(name = "name", description = "Name of a person", example = "Vatsal", required = false) @RequestParam(name = "name", required = false) String name,
            @Parameter(name = "firstName", description = "Firstname of a person", example = "Camille", required = false) @RequestParam(name = "firstname", required = false) String firstname,
            @Parameter(name = "birthdate", description = "Birthdate of a person", example = "22.12.2000", required = false) @RequestParam(name = "birthdate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate birthdate) {

        CheckUtility.validateAtLeastOneNotBlank
                .accept(Arrays.asList(name, firstname,
                        birthdate != null ? birthdate.toString() : null));

        return service.findBy(name, firstname, birthdate).stream()
                .map(PersonResponse.fromPerson)
                .toList();
    }

    /******** Create a person with an existing person/user ********/
    @Operation(summary = "Create a person with an existing person/user", description = "Return created person id")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> createAPerson(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateOrModifyPersonRequest newPersonRequest) {
        long createPersonId = service.createNewPersonWithAnExistingUser(userDetails.getPersonId(),
                newPersonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createPersonId);
    }

    /******** Modify a person ********/
    @Operation(summary = "Modify a person", description = "Modify existing person")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> modify(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable long id,
            @RequestBody CreateOrModifyPersonRequest personRequest) {

        service.modifyPerson(userDetails.getPersonId(), id, personRequest);
        return ResponseEntity.noContent().build();
    }

}
