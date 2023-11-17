package com.api.landtracker.model.dto;

import com.api.landtracker.model.entities.PaymentReason;
import com.api.landtracker.model.entities.ResponseFile;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonView(View.Basic.class)
public class PaymentDTO {

    private Long id;
    private Long lotId;
    private Long clientId;
    private String clientName;
    private Long userId;
    private BigDecimal amount;
    private String username;
    private ResponseFile file;
    private LocalDateTime createdDate;
    private PaymentReason reason;
}
