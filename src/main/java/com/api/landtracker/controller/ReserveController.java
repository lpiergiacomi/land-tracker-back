package com.api.landtracker.controller;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.service.ReserveService;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reserves")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping
    public ReserveDTO saveReserve(@RequestBody ReserveDTO reserve) throws DataValidationException {
        return reserveService.saveReserve(reserve);
    }
    @PutMapping("/update-due-date/{id}")
    public ReserveDTO updateDueDate(@PathVariable Long id, @RequestParam LocalDate dueDate) throws DataValidationException {
        return reserveService.updateDueDate(id, dueDate);
    }
}
