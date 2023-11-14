package com.api.landtracker.controller;

import com.api.landtracker.model.dto.IPieChartData;
import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.dto.View;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.service.LotService;
import com.api.landtracker.utils.exception.DataValidationException;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @GetMapping("/{id}")
    @JsonView(View.Extended.class)
    public LotDTO getLotById(@PathVariable Long id) {
        return lotService.getLotById(id);
    }

    @GetMapping
    @JsonView(View.Basic.class)
    public List<LotDTO> getAllLots() {
        return lotService.getAllLots();
    }
    @PostMapping
    public LotDTO saveLot(@RequestBody LotDTO lot) {
        return lotService.saveLot(lot);
    }

    @PostMapping("/filter")
    @JsonView(View.Basic.class)
    public List<LotDTO> getFilteredLots(@RequestBody LotFilterParams lotParams) {

        return lotService.getAllLotsWithFilter(lotParams);
    }

    @PostMapping("/update-assigned-lots-to-user")
    public UserWithAssignedLotsDTO updateAssignedLotsToUser(@RequestBody UserWithAssignedLotsDTO user) throws DataValidationException {
        return lotService.updateAssignedLotsToUser(user);
    }

   @GetMapping("/lots-quantity-by-state")
   public List<IPieChartData> getLotsQuantityByState() {
        return lotService.getLotsQuantityByState();
   }

}
