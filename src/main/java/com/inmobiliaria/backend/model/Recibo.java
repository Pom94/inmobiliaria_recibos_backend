package com.inmobiliaria.backend.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer numeroRecibo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    private Date fechaRecibo;

    @ElementCollection
    @CollectionTable(name = "recibo_conceptos", joinColumns = @JoinColumn(name = "recibo_id"))
    private List<Concepto> conceptos;

    private Double subtotal;
    @ElementCollection
    @CollectionTable(name = "recibo_mediosPagos", joinColumns = @JoinColumn(name = "recibo_id"))
    private List<MedioPago> mediosPagos;
    private Double total;
    private String pesos;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

}
