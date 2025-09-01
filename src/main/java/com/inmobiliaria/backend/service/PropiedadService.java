package com.inmobiliaria.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.PropiedadRequest;
import com.inmobiliaria.backend.dto.PropiedadResponse;
import com.inmobiliaria.backend.exception.PropiedadNoEncontradaException;
import com.inmobiliaria.backend.model.Propiedad;
import com.inmobiliaria.backend.repository.PropiedadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropiedadService {

    private final PropiedadRepository propiedadRepository;

    public List<PropiedadResponse> listaPropiedades() {
        return propiedadRepository.findAll().stream()
                .filter(Propiedad::isActivo)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PropiedadResponse obtenerPropiedadPorId(Integer id) throws PropiedadNoEncontradaException {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new PropiedadNoEncontradaException("Propiedad con ID " + id + " no encontrada."));
        return mapToResponse(propiedad);
    }

    public PropiedadResponse crearPropiedad(PropiedadRequest request) {
        Propiedad propiedad = Propiedad.builder()
                .numContrato(request.getNumContrato())
                .inicioContrato(request.getInicioContrato())
                .finContrato(request.getFinContrato())
                .nombrePropietario(request.getNombrePropietario())
                .direccionPropietario(request.getDireccionPropietario())
                .localidadPropiedad(request.getLocalidadPropiedad())
                .cuitPropietario(request.getCuitPropietario())
                .activo(true)
                .build();
        propiedad = propiedadRepository.save(propiedad);
        return mapToResponse(propiedad);
    }

    public PropiedadResponse modificarPropiedad(Integer id, PropiedadRequest request) throws PropiedadNoEncontradaException {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new PropiedadNoEncontradaException("Propiedad con ID " + id + " no encontrada."));
        if (request.getNumContrato() != null){
            propiedad.setNumContrato(request.getNumContrato());
        }
        if (request.getInicioContrato() != null) {
            propiedad.setInicioContrato(request.getInicioContrato());
        }
        if (request.getFinContrato() != null){
            propiedad.setFinContrato(request.getFinContrato());
        }
        if (request.getNombrePropietario() != null){
            propiedad.setNombrePropietario(request.getNombrePropietario());
        }
        if (request.getDireccionPropietario() != null){
            propiedad.setDireccionPropietario(request.getDireccionPropietario());
        }
        if (request.getLocalidadPropiedad() != null) {
            propiedad.setLocalidadPropiedad(request.getLocalidadPropiedad());
        }
        if (request.getCuitPropietario() != null){
            propiedad.setCuitPropietario(request.getCuitPropietario());
        }
        
        propiedad = propiedadRepository.save(propiedad);
        return mapToResponse(propiedad);
    }

    private PropiedadResponse mapToResponse(Propiedad propiedad) {
        return PropiedadResponse.builder()
                .id(propiedad.getId())
                .numContrato(propiedad.getNumContrato())
                .inicioContrato(propiedad.getInicioContrato())
                .finContrato(propiedad.getFinContrato())
                .nombrePropietario(propiedad.getNombrePropietario())
                .direccionPropietario(propiedad.getDireccionPropietario())
                .localidadPropiedad(propiedad.getLocalidadPropiedad())
                .cuitPropietario(propiedad.getCuitPropietario())
                .activo(propiedad.isActivo())
                .build();
    }

        public void desactivarPropiedad(Integer id) throws PropiedadNoEncontradaException {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new PropiedadNoEncontradaException("Propiedad con ID " + id + " no encontrada."));
        propiedad.setActivo(false);
        propiedadRepository.save(propiedad);
    }

    public List<PropiedadResponse> listaExPropiedades() {
        return propiedadRepository.findAll().stream()
                .filter(propiedad -> !propiedad.isActivo())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}