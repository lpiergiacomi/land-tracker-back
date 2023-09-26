package com.api.landtracker.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@Table(name = "reserva")
@NoArgsConstructor
@AllArgsConstructor
public class Reserva implements Serializable {

    private static final long serialVersionUID = 2349767485857869704L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "lote", nullable = false)
    private Lote lote;

    @Column(name = "numero", unique = true)
    private String numero;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

}
