package com.skylab.skyticket.api.controllers;

import com.skylab.skyticket.business.abstracts.AuthService;
import com.skylab.skyticket.entities.User;
import com.skylab.skyticket.entities.dtos.auth.LoginDto;
import com.skylab.skyticket.entities.dtos.auth.RegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        var user = User.builder()
                .email(loginDto.getEmail())
                .password(loginDto.getPassword())
                .build();

        var result = authService.login(user);
        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){

        var user = User.builder()
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .build();

        var result = authService.register(user);
        return ResponseEntity.status(result.getHttpStatus()).body(result);
    }





}
