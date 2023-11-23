package com.api.landtracker.service;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.entities.Payment;
import com.api.landtracker.model.entities.PaymentReason;
import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.entities.ReserveState;
import com.api.landtracker.model.mappers.PaymentMapper;
import com.api.landtracker.model.mappers.PaymentMapperImpl;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.PaymentRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReserveRepository reserveRepository;

    @Mock
    private LotRepository lotRepository;

    @Spy
    private PaymentMapper paymentMapper = new PaymentMapperImpl();

    @Mock
    private EntityManager entityManager;

    @Mock
    private FileService fileService;

    @Test
    void testSavePayment() throws IOException, DataValidationException {
        Long lotId = 1L;
        PaymentDTO paymentDTO = createSamplePaymentDTO(lotId);
        MultipartFile file = null;

        Lot lot = createSampleLot(lotId);
        Reserve reserve = createSampleReserve(lot);

        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));
        when(reserveRepository.findByLotId(lotId)).thenReturn(reserve);
        when(paymentRepository.saveAndFlush(any())).thenAnswer(invocationOnMock -> {
            Payment payment = invocationOnMock.getArgument(0);
            payment.setId(1L);
            return payment;
        });

        Payment payment = this.paymentMapper.paymentDTOToPayment(paymentDTO);
        payment.setId(1L);
        when(paymentRepository.findById(any())).thenReturn(Optional.of(payment));


        PaymentDTO result = paymentService.savePayment(paymentDTO, file);

        assertNotNull(result);
        assertEquals(paymentDTO.getAmount(), result.getAmount());
        assertNotNull(result.getId());

        verify(lotRepository, times(1)).findById(lotId);
        verify(reserveRepository, times(1)).findByLotId(lotId);
        verify(paymentRepository, times(1)).saveAndFlush(any());
        verify(fileService, times(0)).store(any(), any());
    }

    private PaymentDTO createSamplePaymentDTO(Long lotId) {
        return PaymentDTO.builder()
                .lotId(lotId)
                .amount(BigDecimal.TEN)
                .reason(PaymentReason.RESERVA)
                .build();
    }

    private Lot createSampleLot(Long lotId) {
        return Lot.builder()
                .id(lotId)
                .state(LotState.RESERVADO)
                .price(BigDecimal.valueOf(100))
                .build();
    }

    private Reserve createSampleReserve(Lot lot) {
        return Reserve.builder()
                .id(1L)
                .client(new Client())
                .lot(lot)
                .state(ReserveState.PENDIENTE_DE_PAGO)
                .build();
    }
}
