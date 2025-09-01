package com.inmobiliaria.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private Integer id;
    private String nombre;
    private String direccion;
    private String iva;
    private String cuit;
    private String localidad;
    private boolean activo;
}
