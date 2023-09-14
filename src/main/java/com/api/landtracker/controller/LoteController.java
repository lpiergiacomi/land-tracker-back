package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.filter.LoteFilterParams;
import com.api.landtracker.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

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

    @GetMapping("/filter")
    public ResponseEntity<Page<LoteDTO>> pages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            @RequestParam(defaultValue = "nombre") String order,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestBody LoteFilterParams movementParams) {

        Page<LoteDTO> movements = loteService.obtenerLotesConFiltro(movementParams,
                PageRequest.of(page, size, Sort.by(order)));
        return ok(movements);
    }

}
