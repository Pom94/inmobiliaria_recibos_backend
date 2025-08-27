package com.inmobiliaria.backend.dto;

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
    private List<ConceptoRequest> conceptos;
    
    private List<MedioPagoRequest> mediosPagos;
    private String pesos;

    private Integer clienteId;
    private Integer propiedadId;
}
