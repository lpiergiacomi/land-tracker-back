package com.api.landtracker.service;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.model.filter.LotSpecification;
import com.api.landtracker.model.mappers.LotMapper;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.repository.UserRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final ReserveRepository reserveRepository;
    private final UserRepository userRepository;
    private final LotMapper lotMapper;
    private final ReserveMapper reserveMapper;

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

        if(lotDTO.getState().equals(LotState.RESERVADO)){
            Reserve reserve = reserveRepository.findByLotId(lotDTO.getId());
            lotDTO.setReserve(reserveMapper.reserveToReserveDTO(reserve));
        }
        return lotDTO;
    }

    public List<LotDTO> getAllLotsWithFilter(LotFilterParams params) {

        Specification<Lot> statesEquals = LotSpecification.stateEqualsIn(params.getStates());
        Specification<Lot> nameLike = LotSpecification.lotNameLike(params.getName());
        Specification<Lot> municipalAccNumberLike = LotSpecification.lotMunicipalAccNumberLike(params.getName());
        Specification<Lot> cadastralAccNumberLike = LotSpecification.lotCadastralAccNumberLike(params.getName());
        Specification<Lot> blockLike = LotSpecification.lotBlockLike(params.getName());
        Specification<Lot> zoneLike = LotSpecification.lotZoneLike(params.getName());

        Page<Lot> lotPage = this.lotRepository.findAll(
                Specification.where(statesEquals)
                .and(nameLike.or(municipalAccNumberLike).or(cadastralAccNumberLike).or(blockLike).or(zoneLike)),
                Pageable.unpaged());

        return new ArrayList<>(lotMapper.lotsToLotsDTO(lotPage.getContent()));
    }

    @Transactional
    public UserWithAssignedLotsDTO updateAssignedLotsToUser(UserWithAssignedLotsDTO user) throws DataValidationException {
        lotRepository.deleteAssignedLotsByUserId(user.getId(), user.getAssignedLotsIds(), user.getAssignedLotsIds().isEmpty());
        User userToSave = userRepository.findById(user.getId()).orElseThrow(
                () -> new DataValidationException("Usuario inexistente")
        );

        List<Lot> assignedLots = lotRepository.findAllByIdIn(user.getAssignedLotsIds());
        userToSave.setAssignedLots(assignedLots);
        userRepository.save(userToSave);

        return user;
    }
}
