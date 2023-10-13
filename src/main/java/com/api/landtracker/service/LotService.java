package com.api.landtracker.service;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.User;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository lotRepository;
    private final LotMapper lotMapper;

    @Transactional(dontRollbackOn = Exception.class)
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

    @Transactional(dontRollbackOn = Exception.class)
    public LotDTO getLotById(Long id) {
        Lot lot = lotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No se encontró un lote con ese id"));
        LotDTO lotDTO = lotMapper.lotToLotDTO(lot);
        return lotDTO;
    }

    @Transactional(dontRollbackOn = Exception.class)
    public Page<LotDTO> getAllLotsWithFilter(LotFilterParams params, Pageable pageable) {

        Specification<Lot> statesEquals = LotSpecification.stateEqualsIn(params.getStates());
        Specification<Lot> nameLike = LotSpecification.lotNameLike(params.getName());
        Specification<Lot> municipalAccNumberLike = LotSpecification.lotMunicipalAccNumberLike(params.getName());
        Specification<Lot> cadastralAccNumberLike = LotSpecification.lotCadastralAccNumberLike(params.getName());
        Specification<Lot> blockLike = LotSpecification.lotBlockLike(params.getName());
        Specification<Lot> zoneLike = LotSpecification.lotZoneLike(params.getName());

        Specification<Lot> priceBetweenMinMax = LotSpecification.priceBetweenMinMax(params.getMinPrice(), params.getMaxPrice());

        Page<Lot> lotPage = this.lotRepository.findAll(
                Specification.where(statesEquals)
                .and(nameLike.or(municipalAccNumberLike).or(cadastralAccNumberLike).or(blockLike).or(zoneLike))
                .and(priceBetweenMinMax),
                pageable);

        List<LotDTO> result = new ArrayList<>();

        result.addAll(lotMapper.lotsToLotsDTO(lotPage.getContent()));

        return new PageImpl<LotDTO>(result, pageable, lotPage.getTotalElements());
    }

    @Transactional
    public UserWithAssignedLotsDTO updateAssignedLotsToUser(UserWithAssignedLotsDTO user) {
        User userToAssign = new User();
        userToAssign.setId(user.getId());
        List<Lot> lotsToAssign = lotRepository.findAllById(user.getAssignedLotsIds());
        List<Lot> lotsToSave = new ArrayList<>();
        lotsToAssign.forEach(lot -> {
            if (!lot.getAssignedUsers().stream().map(User::getId).toList().contains(user.getId())){
                lot.getAssignedUsers().add(userToAssign);
                lotsToSave.add(lot);
            }
        });
        lotRepository.saveAll(lotsToSave);
        return user;
    }
}
