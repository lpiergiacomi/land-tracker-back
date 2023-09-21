package com.api.landtracker.controller;

import com.api.landtracker.model.dto.ReservaDTO;
import com.api.landtracker.model.entities.Reserva;
import com.api.landtracker.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {


    private final ReservaService reservaService;

    @GetMapping
    public List<ReservaDTO> obtenerTodasLasReservas() {
        return reservaService.obtenerTodosLasReservas();
    }

    @PostMapping
    public ReservaDTO guardarReserva(@RequestBody ReservaDTO reserva) {
        return reservaService.guardarReserva(reserva);
    }
}
