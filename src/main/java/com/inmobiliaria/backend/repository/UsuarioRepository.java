package com.inmobiliaria.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inmobiliaria.backend.model.Rol;
import com.inmobiliaria.backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByUsername(String username);
    boolean existsByRol(Rol rol);
}