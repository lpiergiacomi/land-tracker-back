package com.api.landtracker.model.filter;

import com.api.landtracker.model.entities.LotState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LotFilterParams {

    private String name;
    private Double minPrice;
    private Double maxPrice;
    private List<LotState> states;
}
