package com.inmobiliaria.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.ContratoRequest;
import com.inmobiliaria.backend.dto.ContratoResponse;
import com.inmobiliaria.backend.exception.ContratoNoEncontradoException;
import com.inmobiliaria.backend.model.Contrato;
import com.inmobiliaria.backend.repository.ContratoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public List<ContratoResponse> listaContratos() {
        return contratoRepository.findAll().stream()
                .filter(Contrato::isActivo)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ContratoResponse obtenerContratoPorId(Integer id) throws ContratoNoEncontradoException {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ContratoNoEncontradoException("Contrato con ID " + id + " no encontrado."));
        return mapToResponse(contrato);
    }

    public ContratoResponse crearContrato(ContratoRequest request) {
        Contrato contrato = Contrato.builder()
                .numContrato(request.getNumContrato())
                .inicioContrato(request.getInicioContrato())
                .finContrato(request.getFinContrato())
                .nombrePropietario(request.getNombrePropietario())
                .direccionPropiedad(request.getDireccionPropiedad())
                .localidadPropiedad(request.getLocalidadPropiedad())
                .cuitPropietario(request.getCuitPropietario())
                .activo(true)
                .build();
        contrato = contratoRepository.save(contrato);
        return mapToResponse(contrato);
    }

    public ContratoResponse modificarContrato(Integer id, ContratoRequest request) throws ContratoNoEncontradoException {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ContratoNoEncontradoException("Contrato con ID " + id + " no encontrada."));
        if (request.getNumContrato() != null){
            contrato.setNumContrato(request.getNumContrato());
        }
        if (request.getInicioContrato() != null) {
            contrato.setInicioContrato(request.getInicioContrato());
        }
        if (request.getFinContrato() != null){
            contrato.setFinContrato(request.getFinContrato());
        }
        if (request.getNombrePropietario() != null){
            contrato.setNombrePropietario(request.getNombrePropietario());
        }
        if (request.getDireccionPropiedad() != null){
            contrato.setDireccionPropiedad(request.getDireccionPropiedad());
        }
        if (request.getLocalidadPropiedad() != null) {
            contrato.setLocalidadPropiedad(request.getLocalidadPropiedad());
        }
        if (request.getCuitPropietario() != null){
            contrato.setCuitPropietario(request.getCuitPropietario());
        }
        
        contrato = contratoRepository.save(contrato);
        return mapToResponse(contrato);
    }

    private ContratoResponse mapToResponse(Contrato contrato) {
        return ContratoResponse.builder()
                .id(contrato.getId())
                .numContrato(contrato.getNumContrato())
                .inicioContrato(contrato.getInicioContrato())
                .finContrato(contrato.getFinContrato())
                .nombrePropietario(contrato.getNombrePropietario())
                .direccionPropiedad(contrato.getDireccionPropiedad())
                .localidadPropiedad(contrato.getLocalidadPropiedad())
                .cuitPropietario(contrato.getCuitPropietario())
                .activo(contrato.isActivo())
                .build();
    }

        public void desactivarContrato(Integer id) throws ContratoNoEncontradoException {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ContratoNoEncontradoException("Contrato con ID " + id + " no encontrado."));
        contrato.setActivo(false);
        contratoRepository.save(contrato);
    }

    public List<ContratoResponse> listaExContratos() {
        return contratoRepository.findAll().stream()
                .filter(contrato -> !contrato.isActivo())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void activarContrato(Integer id) throws ContratoNoEncontradoException{
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ContratoNoEncontradoException("Contrato con ID " + id + " no encontrado."));
        contrato.setActivo(true);
        contratoRepository.save(contrato);
    }
}