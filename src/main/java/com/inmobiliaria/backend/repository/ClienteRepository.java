package com.inmobiliaria.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inmobiliaria.backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
