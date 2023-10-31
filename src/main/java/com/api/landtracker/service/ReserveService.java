package com.api.landtracker.service;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.*;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.repository.UserRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final LotRepository lotRepository;
    private final UserRepository userRepository;
    private final ReserveMapper mapper;

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

}
