package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.Reserve;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReserveMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "lot.id", target = "lotId")
    @Mapping(source = "lot.name", target = "lotName")
    ReserveDTO reserveToReserveDTO(Reserve reserve);

    @InheritInverseConfiguration
    Reserve reserveDTOToReserve(ReserveDTO reserveDTO);

    List<ReserveDTO> reservesToReservesDTO(List<Reserve> reserves);

}

