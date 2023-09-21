package com.api.landtracker.model.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Builder
@Table(name = "lote")
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "superficie")
    private Integer superficie;

    @Column(name = "estado_lote")
    @Enumerated(EnumType.STRING)
    private EstadoLote estadoLote;

    @Embedded
    private PosicionLote posicionLote;

    @Column(name = "metrosFrente")
    private Double metrosFrente;

    @Column(name = "metrosFondo")
    private Double metrosFondo;

    @Column(name = "nroCuentaCatastral")
    private String nroCuentaCatastral;

    @Column(name = "nroCuentaMunicipal")
    private String nroCuentaMunicipal;

    @Column(name = "tieneLuz")
    private Boolean tieneLuz;

    @Column(name = "tieneAgua")
    private Boolean tieneAgua;

    @Column(name = "precio")
    private Double precio;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente")
    private Cliente cliente;

}
