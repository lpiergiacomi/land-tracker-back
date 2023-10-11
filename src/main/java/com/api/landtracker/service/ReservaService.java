package com.api.landtracker.service;

import com.api.landtracker.model.dto.ReservaDTO;
import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.entities.EstadoLote;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.Reserva;
import com.api.landtracker.model.mappers.ReservaMapper;
import com.api.landtracker.repository.LoteRepository;
import com.api.landtracker.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LoteRepository loteRepository;
    private final ReservaMapper mapper;

    public List<ReservaDTO> obtenerTodosLasReservas() {
        List<Reserva> reservas = (List<Reserva>) reservaRepository.findAll();
        return mapper.reservasToReservasDTO(reservas);
    }
    @Transactional
    public ReservaDTO guardarReserva(ReservaDTO reserva) {
        Reserva newReserva = mapper.reservaDTOToReserva(reserva);
        Lote lote = loteRepository.findById(reserva.getIdLote()).orElseThrow(
                () -> new RuntimeException("No se encontró un lote con ese id"));
        lote.setEstadoLote(EstadoLote.RESERVADO);
        newReserva.setNumero(this.generarNumeroUnico());
        LocalDate fechaCreacion = LocalDate.now();
        newReserva.setFechaCreacion(fechaCreacion);
        newReserva.setFechaVencimiento(fechaCreacion.plus(1, ChronoUnit.WEEKS));
        Cliente cliente = new Cliente();
        cliente.setId(reserva.getIdCliente());
        lote.setCliente(cliente);
        loteRepository.save(lote);
        return mapper.reservaToReservaDTO(reservaRepository.save(newReserva));
    }

    private String generarNumeroUnico() {
        String max = reservaRepository.findMaxNumero();
        if (max == null) {
            return "00000001";
        } else {
            int nuevoNumero = Integer.parseInt(max) + 1;
            return String.format("%08d", nuevoNumero);
        }
    }
}
