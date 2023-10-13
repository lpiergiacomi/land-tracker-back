package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.service.LotService;
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

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @GetMapping("/{id}")
    public LotDTO getLotById(@PathVariable Long id) {
        return lotService.getLotById(id);
    }

    @GetMapping
    public List<LotDTO> getAllLots() {
        return lotService.getAllLots();
    }
    @PostMapping
    public LotDTO saveLot(@RequestBody LotDTO lot) {
        return lotService.saveLot(lot);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<LotDTO>> pages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            @RequestParam(defaultValue = "name") String order,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestBody LotFilterParams lotParams) {

        Page<LotDTO> lots = lotService.getAllLotsWithFilter(lotParams,
                PageRequest.of(page, size, Sort.by(order)));
        return ok(lots);
    }

    @PostMapping("/update-assigned-lots-to-user")
    public UserWithAssignedLotsDTO updateAssignedLotsToUser(@RequestBody UserWithAssignedLotsDTO user) {
        return lotService.updateAssignedLotsToUser(user);
    }

}
