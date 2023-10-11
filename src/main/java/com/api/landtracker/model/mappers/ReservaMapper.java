package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.ReservaDTO;
import com.api.landtracker.model.entities.Reserva;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "cliente.nombre", target = "nombreCliente")
    @Mapping(source = "lote.id", target = "idLote")
    @Mapping(source = "lote.nombre", target = "nombreLote")
    ReservaDTO reservaToReservaDTO(Reserva reserva);

    @InheritInverseConfiguration
    Reserva reservaDTOToReserva(ReservaDTO reservaDTO);

    List<ReservaDTO> reservasToReservasDTO(List<Reserva> reservas);

}

