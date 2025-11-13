package com.inmobiliaria.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String inicio() {
        return "Bienvenido al backend de Inmobiliaria!";
    }

}
