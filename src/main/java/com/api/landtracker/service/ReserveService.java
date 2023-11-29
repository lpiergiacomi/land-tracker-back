package com.api.landtracker.service;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.*;
import com.api.landtracker.model.mappers.LotMapper;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.repository.UserRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final LotRepository lotRepository;
    private final UserRepository userRepository;
    private final ReserveMapper mapper;
    private final LotMapper lotMapper;

    public List<ReserveDTO> getAllReserves() {
        List<Reserve> reserves = (List<Reserve>) reserveRepository.findAll();
        return mapper.reservesToReservesDTO(reserves);
    }

    @Transactional
    public ReserveDTO saveReserve(ReserveDTO reserve) throws DataValidationException {
        Reserve newReserve = mapper.reserveDTOToReserve(reserve);
        User user = userRepository.findById(reserve.getUser().getId()).orElseThrow(
                () -> new DataValidationException("No se encontró el usuario"));

        Lot lot = user.getAssignedLots().stream().filter(l -> l.getId().equals(reserve.getLotId())).findFirst().orElseThrow(
            () -> new DataValidationException("No tenés permisos para reservar este lote"));

        if (!lot.getState().equals(LotState.DISPONIBLE)) {
            throw new DataValidationException("El lote se encuentra en un estado que no puede ser reservado");
        }

        lot.setState(LotState.RESERVADO);
        LocalDate creationDate = LocalDate.now();
        newReserve.setCreatedDate(creationDate);
        newReserve.setUser(user);
        newReserve.setState(ReserveState.PENDIENTE_DE_PAGO);
        Client client = new Client();
        client.setId(reserve.getClientId());
        lotRepository.save(lot);
        return mapper.reserveToReserveDTO(reserveRepository.save(newReserve));
    }

    @Transactional
    public ReserveDTO updateDueDate(Long reserveId, Date dueDate, Long lotId, Long userId)
            throws DataValidationException {
        LocalDate localDueDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Reserve existentReserve = reserveRepository.findById(reserveId).orElseThrow(
                () -> new DataValidationException("No se encontró la reserva"));
        if (localDueDate.isBefore(LocalDate.now())) {
            throw new DataValidationException("La fecha no puede ser anterior al día de hoy");
        }
        validateLotAssignment(lotId, userId);
        existentReserve.setDueDate(localDueDate);
        return mapper.reserveToReserveDTO(reserveRepository.save(existentReserve));
    }

    @Transactional
    public LotDTO cancel(Long reserveId, Long lotId, Long userId) throws DataValidationException {
        Reserve existentReserve = reserveRepository.findById(reserveId).orElseThrow(
                () -> new DataValidationException("No se encontró la reserva"));
        Lot lot = lotRepository.findById(lotId).orElseThrow(
                () -> new DataValidationException("No se encontró el lote"));
        validateLotAssignment(lotId, userId);
        if (lot.getState() != LotState.RESERVADO) {
            throw new DataValidationException("El lote no se encuentra reservado");
        }
        lot.setState(LotState.DISPONIBLE);
        reserveRepository.delete(existentReserve);
        return lotMapper.lotToLotDTO(lotRepository.save(lot));
    }

    private void validateLotAssignment(Long lotId, Long userId) throws DataValidationException {
        lotRepository.findAssignmentLotByUserId(lotId, userId).orElseThrow(
                () -> new DataValidationException("No tenés permisos para gestionar este lote"));
    }
}
