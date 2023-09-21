package com.api.landtracker.model.dto;

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
    private Long clienteId;
    private Long loteId;
    private String nombreLote;
    private String nombreCliente;

}
