package com.marjo.giftyfactoryback.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marjo.giftyfactoryback.auth.UserDetailsImpl;
import com.marjo.giftyfactoryback.error.exception.NoResultException;
import com.marjo.giftyfactoryback.resource.output.UserResponse;
import com.marjo.giftyfactoryback.service.UserService;
import com.marjo.giftyfactoryback.utils.ConverterUtility;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    UserService service;

    /******** Get user connected ********/
    @Operation(summary = "Get connected user", description = "Returns user connected")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No user exists")
    })
    @GetMapping("")
    public UserResponse getUserConnected(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.findById(userDetails.getPersonId())
                .map(ConverterUtility.userToUserResponse)
                .orElseThrow(() -> new NoResultException("An error has occured"));
    }

}
