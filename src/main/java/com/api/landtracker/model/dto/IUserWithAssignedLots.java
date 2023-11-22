package com.api.landtracker.model.dto;

public interface IUserWithAssignedLots {

    Long getId();
    String getUsername();
    String getAssignedLotsIdsString();
}
