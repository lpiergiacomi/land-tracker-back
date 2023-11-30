package com.api.landtracker.service;

import com.api.landtracker.model.dto.IDashboardCard;
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
import com.api.landtracker.repository.DashboardRepository;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.PaymentRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @InjectMocks
    private DashboardService dashboardService;

    @Mock
    private DashboardRepository dashboardRepository;

    @Spy
    PaymentMapper paymentMapper;

    @Test
    void testGetPaymentsByTimeScale() {
        String timeScale = "day";

        when(dashboardRepository.findByCreatedDateBetween(any(), any()))
                .thenReturn(Collections.emptyList());

        List<PaymentDTO> result = dashboardService.getPaymentsByTimeScale(timeScale);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dashboardRepository, times(1)).findByCreatedDateBetween(any(), any());
    }

    @Test
    void testGetDashboardCardsInfo() {
        String timeScale = "week";

        when(dashboardRepository.getDashboardCardsInfoBetweenDates(any(), any()))
                .thenReturn(Collections.emptyList());

        List<IDashboardCard> result = dashboardService.getDashboardCardsInfo(timeScale);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dashboardRepository, times(1)).getDashboardCardsInfoBetweenDates(any(), any());
    }
}
