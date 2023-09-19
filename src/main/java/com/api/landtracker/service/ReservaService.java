package com.api.landtracker.service;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.dto.ReservaDTO;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.Reserva;
import com.api.landtracker.model.mappers.ReservaMapper;
import com.api.landtracker.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper mapper;

    public List<ReservaDTO> obtenerTodosLasReservas() {
        List<Reserva> reservas = (List<Reserva>) reservaRepository.findAll();
        return mapper.reservasToReservasDTO(reservas);
    }
    @Transactional
    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
}
