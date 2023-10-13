package com.api.landtracker.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserWithAssignedLotsDTO {

    private Long id;
    private String username;
    private List<Long> assignedLotsIds;
}
