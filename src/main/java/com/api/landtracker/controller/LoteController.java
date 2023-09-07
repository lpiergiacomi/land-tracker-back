package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
public class LoteController {

    private final LoteService loteService;

    @GetMapping("/{id}")
    public LoteDTO obtenerLotePorId(@PathVariable Long id) {
        LoteDTO lote = loteService.obtenerLotePorId(id);
        return lote;
    }

    @GetMapping
    public List<LoteDTO> obtenerTodosLosLotes() {
        return loteService.obtenerTodosLosLotes();
    }

    @PostMapping
    public LoteDTO guardarLote(@RequestBody LoteDTO lote) {
        return loteService.guardarLote(lote);
    }
}
