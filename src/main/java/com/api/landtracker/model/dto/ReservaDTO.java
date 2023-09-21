package com.api.landtracker.model.dto;

import com.api.landtracker.model.entities.EstadoLote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
