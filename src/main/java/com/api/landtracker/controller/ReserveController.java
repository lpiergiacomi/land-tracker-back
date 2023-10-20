package com.api.landtracker.controller;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.service.ReserveService;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reserves")
@RequiredArgsConstructor
public class ReserveController {


    private final ReserveService reserveService;

    @GetMapping
    public List<ReserveDTO> getAllReserves() {
        return reserveService.getAllReserves();
    }

    @PostMapping
    public ReserveDTO saveReserve(@RequestBody ReserveDTO reserve) throws DataValidationException {
        return reserveService.saveReserve(reserve);
    }
}
