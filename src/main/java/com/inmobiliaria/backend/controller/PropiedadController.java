package com.inmobiliaria.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inmobiliaria.backend.dto.PropiedadRequest;
import com.inmobiliaria.backend.dto.PropiedadResponse;
import com.inmobiliaria.backend.exception.PropiedadNoEncontradaException;
import com.inmobiliaria.backend.service.PropiedadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

    private final PropiedadService propiedadService;

    @GetMapping("/listar")
    public List<PropiedadResponse> listaPropiedades() {
        return propiedadService.listaPropiedades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadResponse> obtenerPropiedadPorId(@PathVariable Integer id) throws PropiedadNoEncontradaException {
        return ResponseEntity.ok(propiedadService.obtenerPropiedadPorId(id));
    }

    @PostMapping("/crear")
    public PropiedadResponse crearPropiedad(@RequestBody PropiedadRequest request) {
        return propiedadService.crearPropiedad(request);
    }

    @PutMapping("/{id}/modificar")
    public ResponseEntity<PropiedadResponse> modificarPropiedad(@PathVariable Integer id, @RequestBody PropiedadRequest request) throws PropiedadNoEncontradaException {
        return ResponseEntity.ok(propiedadService.modificarPropiedad(id, request));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarPropiedad(@PathVariable Integer id) throws PropiedadNoEncontradaException {
        propiedadService.desactivarPropiedad(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inactivas")
    public List<PropiedadResponse> listaExPropiedades() {
        return propiedadService.listaExPropiedades();
    }
}