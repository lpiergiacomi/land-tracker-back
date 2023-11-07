package com.api.landtracker.controller;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.ResponseFile;
import com.api.landtracker.service.FileService;
import com.api.landtracker.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {


    private final PaymentService paymentService;
    private final FileService fileService;

    @PostMapping
    public PaymentDTO savePayment(@RequestParam("file") MultipartFile file, @RequestParam("lotId") Long lotId,
                                  @RequestParam("clientId") Long clientId, @RequestParam("userId") Long userId,
                                  @RequestParam("amount") BigDecimal amount) throws IOException {
        ResponseFile responseFile = fileService.store(file, lotId);
        File insertedFile = new File();
        insertedFile.setId(responseFile.getId());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setClientId(clientId);
        paymentDTO.setUserId(userId);
        paymentDTO.setLotId(lotId);
        paymentDTO.setAmount(amount);

        return this.paymentService.savePayment(paymentDTO, insertedFile);
    }
}
