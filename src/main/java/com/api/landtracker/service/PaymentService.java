package com.api.landtracker.service;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.File;
import com.api.landtracker.model.entities.Payment;
import com.api.landtracker.model.mappers.PaymentMapper;
import com.api.landtracker.repository.PaymentRepository;
import com.api.landtracker.repository.ReserveRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ReserveRepository reserveRepository;
    private final EntityManager entityManager;

    @Transactional(rollbackOn = Exception.class)
    public PaymentDTO savePayment(PaymentDTO payment, File file) {
        payment.setClientId(this.reserveRepository.findByLotId(payment.getLotId()).getClient().getId());
        Payment paymentToSave = this.paymentMapper.paymentDTOToPayment(payment);
        paymentToSave.setFile(file);
        paymentToSave.setCreatedDate(LocalDate.now());

        Payment insertedPayment = this.paymentRepository.saveAndFlush(paymentToSave);
        this.entityManager.refresh(insertedPayment);
        Payment response = this.paymentRepository.findById(insertedPayment.getId()).orElseThrow();
        return this.paymentMapper.paymentToPaymentDTO(response);
    }
}
