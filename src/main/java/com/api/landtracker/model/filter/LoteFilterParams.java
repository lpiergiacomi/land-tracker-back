package com.api.landtracker.model.filter;

import com.api.landtracker.model.entities.EstadoLote;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class LoteFilterParams {

    private String nombre;
    private Double precioMin;
    private Double precioMax;
    private List<EstadoLote> estados;
}
