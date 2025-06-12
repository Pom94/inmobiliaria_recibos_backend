package com.inmobiliaria.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.AuthResponse;
import com.inmobiliaria.backend.dto.CambiarContraseniaRequest;
import com.inmobiliaria.backend.dto.LoginRequest;
import com.inmobiliaria.backend.dto.RegisterRequest;
import com.inmobiliaria.backend.exception.AdminNoEncontradoException;
import com.inmobiliaria.backend.exception.AdminYaExisteException;
import com.inmobiliaria.backend.exception.ContraseniaIncorrectaException;
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

    public AuthResponse login(LoginRequest request) throws AdminNoEncontradoException, ContraseniaIncorrectaException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e){
            throw new ContraseniaIncorrectaException("La contraseña es incorrecta.");
        }
            UserDetails usuario = usuarioRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new AdminNoEncontradoException("Usuario " + request.getUsername() + " no encontrado."));

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

    public void cambiarContrasenia(CambiarContraseniaRequest request) throws ContraseniaIncorrectaException, AdminNoEncontradoException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new AdminNoEncontradoException("Usuario no encontrado."));

        if (!passwordEncoder.matches(request.getContraseniaActual(), usuario.getPassword())){
            throw new ContraseniaIncorrectaException("La contraseña actual es incorrecta.");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNuevaContrasenia()));
        usuarioRepository.save(usuario);
    }

}
