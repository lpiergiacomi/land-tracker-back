package com.api.landtracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaDTO {

    private Long id;
    private Long idCliente;
    private Long idLote;
    private String nombreLote;
    private String nombreCliente;
    private String numero;
    private LocalDate fechaVencimiento;
}
