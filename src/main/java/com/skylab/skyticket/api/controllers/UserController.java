package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/getUserById/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        var result = userService.getUserById(userId);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        var result = userService.getUserByEmail(email);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @GetMapping("/getUserByPhoneNumber/{phoneNumber}")
    public ResponseEntity<?> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        var result = userService.getUserByPhoneNumber(phoneNumber);

        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }



}
