package com.api.landtracker.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@JsonView(View.Basic.class)
public class UserDTO {

    private Long id;
    private String username;
}
