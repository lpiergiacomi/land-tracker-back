package com.api.landtracker.service;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.model.filter.LotSpecification;
import com.api.landtracker.model.mappers.LotMapper;
import com.api.landtracker.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository lotRepository;
    private final LotMapper lotMapper;

    public List<LotDTO> getAllLots() {
        List<Lot> lots = (List<Lot>) lotRepository.findAll();
        List<LotDTO> lotsDTO = lotMapper.lotsToLotsDTO(lots);
        return lotsDTO;
    }
    @Transactional
    public LotDTO saveLot(LotDTO lotDTO) {
        Lot lot = lotMapper.lotDTOToLot(lotDTO);
        LotDTO lotResponse = lotMapper.lotToLotDTO(lotRepository.save(lot));
        return lotResponse;
    }

    public LotDTO getLotById(Long id) {
        Lot lot = lotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No se encontró un lote con ese id"));
        LotDTO lotDTO = lotMapper.lotToLotDTO(lot);
        return lotDTO;
    }

    public Page<LotDTO> getAllLotsWithFilter(LotFilterParams params, Pageable pageable) {

        Specification<Lot> statesEquals = LotSpecification.stateEqualsIn(params.getStates());
        Specification<Lot> nameLike = LotSpecification.lotNameLike(params.getName());
        Specification<Lot> municipalAccNumberLike = LotSpecification.lotMunicipalAccNumberLike(params.getName());
        Specification<Lot> cadastralAccNumberLike = LotSpecification.lotCadastralAccNumberLike(params.getName());
        Specification<Lot> priceBetweenMinMax = LotSpecification.priceBetweenMinMax(params.getMinPrice(), params.getMaxPrice());

        Page<Lot> lotPage = this.lotRepository.findAll(
                Specification.where(statesEquals)
                .and(nameLike.or(municipalAccNumberLike).or(cadastralAccNumberLike))
                .and(priceBetweenMinMax),
                pageable);

        List<LotDTO> result = new ArrayList<>();

        result.addAll(lotMapper.lotsToLotsDTO(lotPage.getContent()));

        return new PageImpl<LotDTO>(result, pageable, lotPage.getTotalElements());
    }
}
