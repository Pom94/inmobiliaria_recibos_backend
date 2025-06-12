package com.inmobiliaria.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inmobiliaria.backend.dto.AuthResponse;
import com.inmobiliaria.backend.dto.CambiarContraseniaRequest;
import com.inmobiliaria.backend.dto.LoginRequest;
import com.inmobiliaria.backend.dto.RegisterRequest;
import com.inmobiliaria.backend.exception.AdminNoEncontradoException;
import com.inmobiliaria.backend.exception.AdminYaExisteException;
import com.inmobiliaria.backend.exception.ContraseniaIncorrectaException;
import com.inmobiliaria.backend.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<AuthResponse> iniciarSesion(@RequestBody LoginRequest request) throws AdminNoEncontradoException, ContraseniaIncorrectaException{
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegisterRequest request) throws AdminYaExisteException{
        return ResponseEntity.ok(authService.registrarAdmin(request));
    }

    @PostMapping("/cambiar-contrasenia")
    public ResponseEntity<String> cambiarContrasenia(@RequestBody CambiarContraseniaRequest request) throws ContraseniaIncorrectaException, AdminNoEncontradoException{
        authService.cambiarContrasenia(request);
        return ResponseEntity.ok("Contraseña cambiada.");
    }

}
