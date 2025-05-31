package com.inmobiliaria.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoResponse {
    private String concepto;
    private String periodo;
    private String anio;
    private Double importe;
}
