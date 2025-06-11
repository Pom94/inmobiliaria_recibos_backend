package com.inmobiliaria.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReciboRequest {
    private String cliente;
    private String direccionCliente;
    private String ivaCliente;
    private String cuitCliente;
    private String localidadCliente;
    
    private String numContrato;
    private Date inicioContrato;
    private Date finContrato;
    private String propietario;
    private String callePropiedad;
    private String localidadPropiedad;
    private String cuitPropietario;
    
    private List<ConceptoRequest> conceptos;
    
    private List<MedioPagoRequest> mediosPagos;
    private String pesos;
}
