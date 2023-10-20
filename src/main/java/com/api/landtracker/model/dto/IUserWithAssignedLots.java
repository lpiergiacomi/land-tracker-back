package com.api.landtracker.model.dto;

import java.util.List;

public interface IUserWithAssignedLots {

    Long getId();
    String getUsername();
    String getAssignedLotsIdsString();
}
