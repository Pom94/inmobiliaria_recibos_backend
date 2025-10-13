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

import com.inmobiliaria.backend.dto.ClienteRequest;
import com.inmobiliaria.backend.dto.ClienteResponse;
import com.inmobiliaria.backend.exception.ClienteNoEncontradoException;
import com.inmobiliaria.backend.service.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/listar")
    public List<ClienteResponse> listaClientes() {
        return clienteService.listaClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtenerClientePorId(@PathVariable Integer id) throws ClienteNoEncontradoException {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    @PostMapping("/crear")
    public ClienteResponse crearCliente(@RequestBody ClienteRequest request) {
        return clienteService.crearCliente(request);
    }

    @PutMapping("/{id}/modificar")
    public ResponseEntity<ClienteResponse> modificarCliente(@PathVariable Integer id, @RequestBody ClienteRequest request) throws ClienteNoEncontradoException {
        return ResponseEntity.ok(clienteService.modificarCliente(id, request));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarCliente(@PathVariable Integer id) throws ClienteNoEncontradoException {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inactivos")
    public List<ClienteResponse> listaExClientes() {
        return clienteService.listaExClientes();
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarCliente(@PathVariable Integer id) throws ClienteNoEncontradoException {
        clienteService.activarCliente(id);
        return ResponseEntity.noContent().build();
    }
}