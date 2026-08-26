package com.travelplanner.controller;

import com.travelplanner.dto.SignupRequest;
import com.travelplanner.entity.User;
import com.travelplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> signup(
            @Valid @RequestBody SignupRequest request) {

        User user = userService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
}