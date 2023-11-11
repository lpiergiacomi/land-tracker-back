package com.api.landtracker.model.entities;

import com.api.landtracker.model.dto.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonView(View.Basic.class)
public class ResponseFile {
    private String name;
    private String id;
    private String type;
    private long size;
}
