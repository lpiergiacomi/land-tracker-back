package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.ReservaDTO;
import com.api.landtracker.model.entities.Reserva;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "lote.id", target = "loteId")
    ReservaDTO reservaToReservaDTO(Reserva reserva);

    @InheritInverseConfiguration
    Reserva reservaDTOToReserva(ReservaDTO reservaDTO);

    List<ReservaDTO> reservasToReservasDTO(List<Reserva> reservas);

}

