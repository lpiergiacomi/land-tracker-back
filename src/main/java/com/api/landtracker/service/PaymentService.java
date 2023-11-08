package com.api.landtracker.service;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.*;
import com.api.landtracker.model.mappers.PaymentMapper;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.PaymentRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReserveRepository reserveRepository;
    private final LotRepository lotRepository;
    private final PaymentMapper paymentMapper;
    private final EntityManager entityManager;
    private final FileService fileService;

    @Transactional(rollbackOn = Exception.class)
    public PaymentDTO savePayment(PaymentDTO payment, MultipartFile file) throws IOException, DataValidationException {
        Lot lot = this.lotRepository.findById(payment.getLotId()).orElseThrow(
                () -> new DataValidationException("No se encontró el lote"));

        this.validateLot(lot, payment.getAmount());
        File insertedFile = null;

        if (file != null) {
            ResponseFile responseFile = fileService.store(file, payment.getLotId());
            insertedFile = new File();
            insertedFile.setId(responseFile.getId());
        }

        payment.setClientId(this.reserveRepository.findByLotId(payment.getLotId()).getClient().getId());
        Payment paymentToSave = this.paymentMapper.paymentDTOToPayment(payment);
        paymentToSave.setFile(insertedFile);
        paymentToSave.setCreatedDate(LocalDate.now());

        Payment insertedPayment = this.paymentRepository.saveAndFlush(paymentToSave);
        this.entityManager.refresh(insertedPayment);

        this.checkStateChange(lot);

        Payment response = this.paymentRepository.findById(insertedPayment.getId()).orElseThrow();
        return this.paymentMapper.paymentToPaymentDTO(response);
    }

    private void validateLot(Lot lot, BigDecimal newAmount) throws DataValidationException {
        if (lot.getState() != LotState.RESERVADO) {
            throw new DataValidationException("El lote no se encuentra en un estado para añadirle un pago");
        }
        BigDecimal sumOfPayments = getSumOfPayments(lot);
        if (sumOfPayments.add(newAmount).compareTo(lot.getPrice()) > 0) {
            throw new DataValidationException("Estás superando el precio del lote");
        }
    }

    private BigDecimal getSumOfPayments(Lot lot) {
        return this.paymentRepository.findAllByLotId(lot.getId())
                .stream().map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void checkStateChange(Lot lot) {
        BigDecimal sumOfPayments = getSumOfPayments(lot);
        if (sumOfPayments.compareTo(lot.getPrice()) == 0) {
            lot.setState(LotState.VENDIDO);
            lotRepository.save(lot);
        }
    }
}
