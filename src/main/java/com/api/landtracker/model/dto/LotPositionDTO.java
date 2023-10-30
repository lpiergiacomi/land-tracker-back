package com.api.landtracker.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonView(View.Basic.class)
public class LotPositionDTO {

    private Double x;
    private Double y;
    private Double z;
}
