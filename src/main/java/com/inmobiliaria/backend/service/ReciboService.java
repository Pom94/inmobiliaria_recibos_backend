package com.inmobiliaria.backend.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inmobiliaria.backend.dto.ConceptoRequest;
import com.inmobiliaria.backend.dto.ConceptoResponse;
import com.inmobiliaria.backend.dto.ReciboRequest;
import com.inmobiliaria.backend.dto.ReciboResponse;
import com.inmobiliaria.backend.exception.AdminNoEncontradoException;
import com.inmobiliaria.backend.model.Concepto;
import com.inmobiliaria.backend.model.Recibo;
import com.inmobiliaria.backend.model.Usuario;
import com.inmobiliaria.backend.repository.ReciboRepository;
import com.inmobiliaria.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReciboService {

    private final UsuarioRepository usuarioRepository;
    private final ReciboRepository reciboRepository;

    public ReciboResponse crearRecibo(ReciboRequest request) throws AdminNoEncontradoException{
        Usuario usuario = usuarioRepository.findByUsername("admin")
        .orElseThrow(() -> new AdminNoEncontradoException("Usuario admin no encontrado."));

        List<Concepto> conceptosEntidad = request.getConceptos().stream()
            .map(this::mapearConcepto)
            .collect(Collectors.toList());

        Recibo recibo = construirRecibo(request, usuario, conceptosEntidad);

        recibo = reciboRepository.save(recibo);

        recibo.setNumeroRecibo(recibo.getId());

        recibo = reciboRepository.save(recibo);

        return mapearAResponse(recibo);

    }

    public List<ReciboResponse> listarTodos() {
        return reciboRepository.findAll().stream()
        .map(this::mapearAResponse)
        .collect(Collectors.toList());
    }

    private Recibo construirRecibo(ReciboRequest request, Usuario usuario, List<Concepto> conceptos){
        return Recibo.builder()
        .usuario(usuario)
        .fechaRecibo(new Date())
        .conceptos(conceptos)
        .cliente(request.getCliente())
        .direccionCliente(request.getDireccionCliente())
        .ivaCliente(request.getIvaCliente())
        .cuitCliente(request.getCuitCliente())
        .localidadCliente(request.getLocalidadCliente())
        .numContrato(request.getNumContrato())
        .inicioContrato(request.getInicioContrato())
        .finContrato(request.getFinContrato())
        .propietario(request.getPropietario())
        .callePropiedad(request.getCallePropiedad())
        .localidadPropiedad(request.getLocalidadPropiedad())
        .cuitPropietario(request.getCuitPropietario())
        .subtotal(request.getSubtotal())
        .medioPago(request.getMedioPago())
        .importePago(request.getImportePago())
        .total(request.getTotal())
        .pesos(request.getPesos())
        .build();
    }

    private Concepto mapearConcepto(ConceptoRequest request){
        return Concepto.builder()
                    .concepto(request.getConcepto())
                    .periodo(request.getPeriodo())
                    .anio(request.getAnio())
                    .importe(request.getImporte())
                    .build();
    }

    private ReciboResponse mapearAResponse(Recibo recibo){
        return ReciboResponse.builder()
        .numeroRecibo(recibo.getNumeroRecibo())
        .fechaRecibo(recibo.getFechaRecibo())
        .cliente(recibo.getCliente())
        .direccionCliente(recibo.getDireccionCliente())
        .ivaCliente(recibo.getIvaCliente())
        .cuitCliente(recibo.getCuitCliente())
        .localidadCliente(recibo.getLocalidadCliente())
        .numContrato(recibo.getNumContrato())
        .inicioContrato(recibo.getInicioContrato())
        .finContrato(recibo.getFinContrato())
        .propietario(recibo.getPropietario())
        .callePropiedad(recibo.getCallePropiedad())
        .localidadPropiedad(recibo.getLocalidadPropiedad())
        .cuitPropietario(recibo.getCuitPropietario())
        .conceptos(mapearConceptosAResponse(recibo.getConceptos()))
        .subtotal(recibo.getSubtotal())
        .medioPago(recibo.getMedioPago())
        .importePago(recibo.getImportePago())
        .total(recibo.getTotal())
        .pesos(recibo.getPesos())
        .build();
    }

    private List<ConceptoResponse> mapearConceptosAResponse(List<Concepto> conceptos){
        return conceptos.stream()
        .map(c -> ConceptoResponse.builder()
        .concepto(c.getConcepto())
        .periodo(c.getPeriodo())
        .anio(c.getAnio())
        .importe(c.getImporte())
        .build())
        .collect(Collectors.toList());
    }

}
