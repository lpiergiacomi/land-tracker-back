package com.api.landtracker.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseFile {
    private String name;
    private String id;
    private String type;
    private long size;
}
