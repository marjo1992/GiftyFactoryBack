package com.marjo.giftyfactoryback.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.marjo.giftyfactoryback.auth.UserDetailsImpl;
import com.marjo.giftyfactoryback.error.exception.NoResultException;
import com.marjo.giftyfactoryback.resource.input.CreateOrModifyIdeaRequest;
import com.marjo.giftyfactoryback.resource.output.IdeaResponse;
import com.marjo.giftyfactoryback.service.IdeaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/idea")
public class IdeaResource {

    @Autowired
    IdeaService service;

    /******** Get idea by id ********/
    @Operation(summary = "Get an idea by id", description = "Returns an idea as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No idea exists with id xxx")
    })
    @GetMapping("/{id}")
    public IdeaResponse getIdea(
            @PathVariable("id") @Parameter(name = "id", description = "Idea id", example = "1") long id) {
        return service.findById(id)
                .map(IdeaResponse.fromIdea)
                .orElseThrow(() -> new NoResultException("No idea exists with id -'" + id));
    }

    /******** Create a gift idea ********/
    @Operation(summary = "Create a gift idea", description = "Return created idea id")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> createAGiftIdea(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateOrModifyIdeaRequest newIdeaRequest) {

        // TODO : tester :
        // * uniquement si la personne pour qui est la liste est lié par un lien valide
        // à
        // - la personne correspondante de l'utilisateur qui appel le service
        // - ou une des personnes pour lequel l'utilisateur est responsble
        // * s'il y a une liste choisi, elle doit être visible par la personne
        long createdIdeaId = service.createIdea(userDetails.getPersonId(), newIdeaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIdeaId);
    }

}
