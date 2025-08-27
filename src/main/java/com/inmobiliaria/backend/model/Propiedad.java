package com.inmobiliaria.backend.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "propiedad")

public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String numContrato;
    private Date inicioContrato;
    private Date finContrato;
    private String nombrePropietario;
    private String direccionPropietario;
    private String localidadPropiedad;
    private String cuitPropietario;
    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
    private List<Recibo> recibos;
}
