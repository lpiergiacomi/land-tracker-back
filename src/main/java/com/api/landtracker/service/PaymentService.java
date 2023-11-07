package com.api.landtracker.service;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.Payment;
import com.api.landtracker.model.mappers.PaymentMapper;
import com.api.landtracker.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional(rollbackOn = Exception.class)
    public PaymentDTO savePayment(PaymentDTO payment, File file){
        Payment paymentToSave = this.paymentMapper.paymentDTOToPayment(payment);
        paymentToSave.setFile(file);

        Payment insertedPayment = this.paymentRepository.save(paymentToSave);
        Payment response = this.paymentRepository.findById(insertedPayment.getId()).orElseThrow();
        return this.paymentMapper.paymentToPaymentDTO(response);
    }
}
