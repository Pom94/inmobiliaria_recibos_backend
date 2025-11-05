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

import com.inmobiliaria.backend.dto.ContratoRequest;
import com.inmobiliaria.backend.dto.ContratoResponse;
import com.inmobiliaria.backend.exception.ContratoNoEncontradoException;
import com.inmobiliaria.backend.service.ContratoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;

    @GetMapping("/listar")
    public List<ContratoResponse> listaContratos() {
        return contratoService.listaContratos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponse> obtenerContratoPorId(@PathVariable Integer id) throws ContratoNoEncontradoException {
        return ResponseEntity.ok(contratoService.obtenerContratoPorId(id));
    }

    @PostMapping("/crear")
    public ContratoResponse crearContrato(@RequestBody ContratoRequest request) {
        return contratoService.crearContrato(request);
    }

    @PutMapping("/{id}/modificar")
    public ResponseEntity<ContratoResponse> modificarContrato(@PathVariable Integer id, @RequestBody ContratoRequest request) throws ContratoNoEncontradoException {
        return ResponseEntity.ok(contratoService.modificarContrato(id, request));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarContrato(@PathVariable Integer id) throws ContratoNoEncontradoException {
        contratoService.desactivarContrato(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inactivos")
    public List<ContratoResponse> listaExContratos() {
        return contratoService.listaExContratos();
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarContrato(@PathVariable Integer id) throws ContratoNoEncontradoException {
        contratoService.activarContrato(id);
        return ResponseEntity.noContent().build();
    }
}