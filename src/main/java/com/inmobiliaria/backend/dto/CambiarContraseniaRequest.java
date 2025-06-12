package com.inmobiliaria.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambiarContraseniaRequest {
    private String contraseniaActual;
    private String nuevaContrasenia;
}
