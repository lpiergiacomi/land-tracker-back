package com.api.landtracker.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(View.Basic.class)
public class ReserveDTO {

    private Long id;
    private Long clientId;
    private Long lotId;
    private String lotName;
    private String clientName;
    private LocalDate dueDate;
    private UserDTO user;
}
