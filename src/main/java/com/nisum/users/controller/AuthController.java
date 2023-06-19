package com.nisum.users.controller;

import com.nisum.users.model.request.LoginRequest;
import com.nisum.users.model.request.SignupRequest;
import com.nisum.users.model.response.UserCreatedResponse;
import com.nisum.users.service.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserServiceImpl userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCreatedResponse> registerUser(
            @Valid @RequestBody SignupRequest signUpRequest) {

        UserCreatedResponse user = this.userService.creteUser(signUpRequest);
        return ResponseEntity.created(URI.create("/user/" + user.getId()))
                .body(user);
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCreatedResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        UserCreatedResponse user = this.userService.authenticateUser(loginRequest);
        return ResponseEntity.ok(user);
    }
}
