package com.api.landtracker.controller;

import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.PaymentReason;
import com.api.landtracker.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
@ActiveProfiles("test")
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentController paymentController;

    @MockBean
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testSavePayment() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test file content".getBytes());
        Long lotId = 1L;
        Long userId = 2L;
        BigDecimal amount = new BigDecimal("100.00");
        String reason = "ADELANTO";

        PaymentDTO paymentDTO = PaymentDTO.builder()
                .lotId(lotId)
                .userId(userId)
                .amount(amount)
                .reason(PaymentReason.ADELANTO)
                .build();

        when(paymentService.savePayment(any(), any())).thenReturn(paymentDTO);

        mockMvc.perform(multipart("/payments")
                        .file(file)
                        .param("lotId", String.valueOf(lotId))
                        .param("userId", String.valueOf(userId))
                        .param("amount", amount.toString())
                        .param("reason", reason))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lotId", is(lotId.intValue())))
                .andExpect(jsonPath("$.userId", is(userId.intValue())))
                .andExpect(jsonPath("$.amount", is(amount.doubleValue())))
                .andExpect(jsonPath("$.reason", is(reason.toUpperCase())));

        verify(paymentService, times(1)).savePayment(any(), any());
    }

    @Test
    void testGetByLotId() throws Exception {
        Long lotId = 1L;

        List<PaymentDTO> payments = Arrays.asList(
                PaymentDTO.builder()
                        .id(1L)
                        .lotId(lotId)
                        .amount(new BigDecimal("100.00"))
                        .reason(PaymentReason.ADELANTO)
                        .build(),
                PaymentDTO.builder()
                        .id(2L)
                        .lotId(lotId)
                        .amount(new BigDecimal("150.00"))
                        .reason(PaymentReason.RESERVA)
                        .build()
        );

        when(paymentService.getByLotId(lotId)).thenReturn(payments);

        mockMvc.perform(get("/payments/lot/{lotId}", lotId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(payments.size())))
                .andExpect(jsonPath("$[0].lotId", is(lotId.intValue())))
                .andExpect(jsonPath("$[0].amount", is(100.00)))
                .andExpect(jsonPath("$[0].reason", is("ADELANTO")))
                .andExpect(jsonPath("$[1].lotId", is(lotId.intValue())))
                .andExpect(jsonPath("$[1].amount", is(150.00)))
                .andExpect(jsonPath("$[1].reason", is("RESERVA")));

        verify(paymentService, times(1)).getByLotId(lotId);
    }
}
