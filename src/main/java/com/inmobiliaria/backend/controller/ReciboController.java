package com.inmobiliaria.backend.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inmobiliaria.backend.dto.ReciboRequest;
import com.inmobiliaria.backend.dto.ReciboResponse;
import com.inmobiliaria.backend.exception.AdminNoEncontradoException;
import com.inmobiliaria.backend.service.ReciboService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recibo")
@RequiredArgsConstructor
public class ReciboController {

    private final ReciboService reciboService;
    
    @PostMapping("/crear")
    private ResponseEntity<ReciboResponse> crearRecibo(@RequestBody ReciboRequest request) throws AdminNoEncontradoException, IOException{
        ReciboResponse creado = reciboService.crearRecibo(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.getNumeroRecibo())
            .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ReciboResponse>> listaRecibo(){
        return ResponseEntity.ok(reciboService.listarTodos());
    }
}
