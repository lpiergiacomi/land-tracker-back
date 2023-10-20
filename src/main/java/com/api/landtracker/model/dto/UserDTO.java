package com.api.landtracker.model.dto;

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
public class UserDTO {

    private Long id;
    private String username;
}
