package com.api.landtracker.model.dto;

import com.api.landtracker.model.entities.LotState;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(View.Basic.class)
public class LotDTO {

    private Long id;
    private String name;
    private Integer area;
    private LotState state;
    private LotPositionDTO position;
    private Double metersFront;
    private Double metersBack;
    private String cadastralAccNumber;
    private String municipalAccNumber;
    private Boolean hasLight;
    private Boolean hasWater;
    private Double price;
    private String block;
    private String zone;
    private Date saleDate;
    @JsonView(View.Extended.class)
    private ReserveDTO reserve;
    private List<PaymentDTO> payments;


}
