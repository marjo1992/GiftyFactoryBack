package com.marjo.giftyfactoryback.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.marjo.giftyfactoryback.auth.JwtUtils;
import com.marjo.giftyfactoryback.auth.UserDetailsImpl;
import com.marjo.giftyfactoryback.resource.input.LoginRequest;
import com.marjo.giftyfactoryback.resource.input.SignupRequest;
import com.marjo.giftyfactoryback.resource.output.JwtResponse;
import com.marjo.giftyfactoryback.resource.output.MessageResponse;
import com.marjo.giftyfactoryback.service.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonService personService;

    @Autowired
    JwtUtils jwtUtils;

    @Operation(summary = "Signin", description = "Authenticate a user and get a token to call other services")
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getPersonId(),
                userDetails.getUsername(),
                userDetails.getEmail()));
    }

    @Operation(summary = "Signup", description = "Create a new user and associated person")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        personService.createNewUser(signUpRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
