package com.api.landtracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotPositionDTO {

    private Double x;
    private Double y;
    private Double z;
}
