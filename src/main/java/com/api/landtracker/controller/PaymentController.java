package com.api.landtracker.controller;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.PaymentReason;
import com.api.landtracker.service.PaymentService;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {


    private final PaymentService paymentService;

    @PostMapping
    public PaymentDTO savePayment(@RequestParam(value = "file", required = false) MultipartFile file,
                                  @RequestParam("lotId") Long lotId,
                                  @RequestParam("userId") Long userId,
                                  @RequestParam("amount") BigDecimal amount,
                                  @RequestParam("reason") String reason)
            throws IOException, DataValidationException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setUserId(userId);
        paymentDTO.setLotId(lotId);
        paymentDTO.setAmount(amount);
        paymentDTO.setReason(PaymentReason.valueOf(reason.toUpperCase()));

        return this.paymentService.savePayment(paymentDTO, file);
    }
}
