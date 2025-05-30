package com.inmobiliaria.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.AuthResponse;
import com.inmobiliaria.backend.dto.LoginRequest;
import com.inmobiliaria.backend.dto.RegisterRequest;
import com.inmobiliaria.backend.exception.AdminYaExisteException;
import com.inmobiliaria.backend.jwt.JwtService;
import com.inmobiliaria.backend.model.Rol;
import com.inmobiliaria.backend.model.Usuario;
import com.inmobiliaria.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtService.getToken(usuario);
            return AuthResponse.builder()
            .token(token)
            .build();
    }

    public AuthResponse registrarAdmin(RegisterRequest request) throws AdminYaExisteException{
        boolean existeAdmin = usuarioRepository.existsByRol(Rol.ADMIN);

        if (existeAdmin) {
            throw new AdminYaExisteException("Ya existe un administrador registrado.");
        }

        Usuario usuario = Usuario.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .rol(Rol.ADMIN)
        .build();

        usuarioRepository.save(usuario);

        return AuthResponse.builder()
        .token(jwtService.getToken(usuario))
        .build();
    }

}
