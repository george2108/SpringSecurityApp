package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
// habilita la seguridad a nivel metodo
@PreAuthorize("denyAll()")
public class TestAuthController {
    @GetMapping
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet(){
        return "Hello world - GET";
    }

    @PostMapping
    // PODEMOS Poner mas de un permiso
    @PreAuthorize("hasAuthority('READ') or hasAuthority('CREATE')")
    public String helloPost(){
        return "Hello world - POST";
    }

    @PutMapping
    public String helloPut(){
        return "Hello world - PUT";
    }

    @DeleteMapping
    public String helloDelete(){
        return "Hello world - DELETE";
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch(){
        return "Hello world - PATCH";
    }
}
