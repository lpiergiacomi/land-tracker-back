package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.entities.Lot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LotMapper {


    LotDTO lotToLotDTO(Lot lot);

    List<LotDTO> lotsToLotsDTO(List<Lot> lots);

    @Mapping(target = "position.x", source = "position.x")
    @Mapping(target = "position.y", source = "position.y")
    @Mapping(target = "position.z", source = "position.z")
    Lot lotDTOToLot(LotDTO lotDTO);

    List<Lot> lotsDTOToLots(List<LotDTO> lotsDTO);

}
