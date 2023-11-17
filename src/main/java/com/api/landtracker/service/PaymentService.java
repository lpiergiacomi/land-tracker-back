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
import java.time.*;
import java.util.*;

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

        this.validateLot(lot, payment);
        File insertedFile = null;

        if (file != null) {
            ResponseFile responseFile = fileService.store(file, payment.getLotId());
            insertedFile = new File();
            insertedFile.setId(responseFile.getId());
        }

        Reserve reserve = this.reserveRepository.findByLotId(payment.getLotId());
        payment.setClientId(reserve.getClient().getId());
        Payment paymentToSave = this.paymentMapper.paymentDTOToPayment(payment);
        paymentToSave.setFile(insertedFile);
        paymentToSave.setCreatedDate(LocalDateTime.now());

        Payment insertedPayment = this.paymentRepository.saveAndFlush(paymentToSave);
        this.entityManager.refresh(insertedPayment);

        this.checkStateChangeLot(lot);
        this.checkStateChangeReserve(reserve, payment);

        Payment response = this.paymentRepository.findById(insertedPayment.getId()).orElseThrow();
        return this.paymentMapper.paymentToPaymentDTO(response);
    }

    private void validateLot(Lot lot, PaymentDTO payment) throws DataValidationException {
        if (lot.getState() != LotState.RESERVADO) {
            throw new DataValidationException("El lote no se encuentra en un estado para añadirle un pago");
        }
        BigDecimal sumOfPayments = getSumOfPayments(lot);
        if (sumOfPayments.add(payment.getAmount()).compareTo(lot.getPrice()) > 0) {
            throw new DataValidationException("Estás superando el precio del lote");
        }
        if (payment.getReason().equals(PaymentReason.RESERVA) && havePaymentForReserve(lot)) {
            throw new DataValidationException("Ya existe un pago en concepto de reserva");
        }
    }

    private List<Payment> getPaymentsByLot(Lot lot) {
        return this.paymentRepository.findAllByLotId(lot.getId());
    }

    private BigDecimal getSumOfPayments(Lot lot) {
        List<Payment> payments = this.getPaymentsByLot(lot);
        return payments.stream().map(Payment::getAmount)
                       .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean havePaymentForReserve(Lot lot) {
        return this.getPaymentsByLot(lot).stream().anyMatch(payment -> payment.getReason().equals(PaymentReason.RESERVA));
    }

    private void checkStateChangeLot(Lot lot) {
        BigDecimal sumOfPayments = getSumOfPayments(lot);
        if (sumOfPayments.compareTo(lot.getPrice()) == 0) {
            lot.setState(LotState.VENDIDO);
            lot.setSaleDate(new Date());
            lotRepository.save(lot);
        }
    }

    private void checkStateChangeReserve(Reserve reserve, PaymentDTO payment) {
        if (payment.getReason().equals(PaymentReason.RESERVA)) {
            reserve.setState(ReserveState.ABONADA);
            reserveRepository.save(reserve);
        }
    }

    public List<PaymentDTO> getByLotId(Long lotId) {
        List<Payment> payments = this.paymentRepository.findAllByLotId(lotId);
        return paymentMapper.paymentsToPaymentsDTO(payments);
    }
}
