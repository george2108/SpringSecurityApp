package com.app.controller;

import com.app.dto.AuthLoginRequestDTO;
import com.app.dto.AuthResponseDTO;
import com.app.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid AuthLoginRequestDTO userRequest
    ){
        return new ResponseEntity<>(
                this.userDetailsService.loginUser(userRequest),
                HttpStatus.OK
        );
    }
}
