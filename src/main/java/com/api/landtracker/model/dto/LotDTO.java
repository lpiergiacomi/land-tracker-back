package com.api.landtracker.model.dto;

import com.api.landtracker.model.entities.LotState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private List<UserDTO> assignedUsers;


}
