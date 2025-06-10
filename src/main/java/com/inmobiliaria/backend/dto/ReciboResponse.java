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
public class ReciboResponse {
    private Integer numeroRecibo;
    private Date fechaRecibo;
    
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
    
    private List<ConceptoResponse> conceptos;
    
    private Double subtotal;
    private List<MedioPagoResponse> mediosPagos;
    /*private String medioPago;
    private Double importePago;*/
    private Double total;
    private String pesos;
}
