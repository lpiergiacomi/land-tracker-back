package com.api.landtracker.service;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final LotRepository lotRepository;
    private final ReserveMapper mapper;

    public List<ReserveDTO> getAllReserves() {
        List<Reserve> reserves = (List<Reserve>) reserveRepository.findAll();
        return mapper.reservesToReservesDTO(reserves);
    }
    @Transactional
    public ReserveDTO saveReserve(ReserveDTO reserve) {
        Reserve newReserve = mapper.reserveDTOToReserve(reserve);
        Lot lot = lotRepository.findById(reserve.getLotId()).orElseThrow(
                () -> new RuntimeException("No se encontró un lote con ese id"));
        lot.setState(LotState.RESERVADO);
        newReserve.setNumber(this.generateUniqueNumber());
        LocalDate creationDate = LocalDate.now();
        newReserve.setCreationDate(creationDate);
        newReserve.setDueDate(creationDate.plus(1, ChronoUnit.WEEKS));
        Client client = new Client();
        client.setId(reserve.getClientId());
        lot.setClient(client);
        lotRepository.save(lot);
        return mapper.reserveToReserveDTO(reserveRepository.save(newReserve));
    }

    private String generateUniqueNumber() {
        String max = reserveRepository.findMaxNumber();
        if (max == null) {
            return "00000001";
        } else {
            int newNumber = Integer.parseInt(max) + 1;
            return String.format("%08d", newNumber);
        }
    }
}
