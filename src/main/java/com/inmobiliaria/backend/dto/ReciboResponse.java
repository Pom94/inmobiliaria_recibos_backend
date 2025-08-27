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
    
    private List<ConceptoResponse> conceptos;
    
    private Double subtotal;
    private List<MedioPagoResponse> mediosPagos;
    private Double total;
    private String pesos;
    private Integer clienteId;
    private Integer propiedadId;
}
