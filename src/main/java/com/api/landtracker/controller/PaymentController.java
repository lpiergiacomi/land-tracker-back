package com.api.landtracker.controller;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.service.PaymentService;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {


    private final PaymentService paymentService;

    @PostMapping
    public PaymentDTO savePayment(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("lotId") Long lotId,
                                  @RequestParam("userId") Long userId,
                                  @RequestParam("amount") BigDecimal amount) throws IOException, DataValidationException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setUserId(userId);
        paymentDTO.setLotId(lotId);
        paymentDTO.setAmount(amount);

        return this.paymentService.savePayment(paymentDTO, file);
    }
}
