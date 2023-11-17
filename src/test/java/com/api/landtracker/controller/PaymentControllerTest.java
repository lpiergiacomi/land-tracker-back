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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

}
