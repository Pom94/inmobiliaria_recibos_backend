package com.inmobiliaria.backend.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratoResponse {
    private Integer id;
    private String numContrato;
    private Date inicioContrato;
    private Date finContrato;
    private String nombrePropietario;
    private String direccionPropiedad;
    private String localidadPropiedad;
    private String cuitPropietario;
    private boolean activo;
}
