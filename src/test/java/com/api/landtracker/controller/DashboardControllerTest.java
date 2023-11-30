package com.api.landtracker.controller;

import com.api.landtracker.model.dto.IDashboardCard;
import com.api.landtracker.model.dto.IReserveCalendar;
import com.api.landtracker.model.dto.PaymentChartDTO;
import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.PaymentReason;
import com.api.landtracker.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
@ActiveProfiles("test")
public class DashboardControllerTest {

    @InjectMocks
    private DashboardController dashboardController;

    @Mock
    private DashboardService dashboardService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    void testGetPaymentChartDataByDay() throws Exception {
        String timeScale = "day";
        List<PaymentDTO> paymentDTOList = Collections.singletonList(
                PaymentDTO.builder()
                        .id(1L)
                        .lotId(2L)
                        .clientId(3L)
                        .clientName("Client 1")
                        .userId(4L)
                        .amount(BigDecimal.TEN)
                        .username("user1")
                        .file(null)
                        .createdDate(LocalDateTime.now().withHour(15).withMinute(10).withSecond(0).withNano(0))
                        .reason(PaymentReason.RESERVA)
                        .build()
        );

        when(dashboardService.getPaymentsByTimeScale(timeScale)).thenReturn(paymentDTOList);

        MvcResult result = mockMvc.perform(get("/dashboard/charts-info/{timeScale}", timeScale))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].label").value("01hs"))
                .andExpect(jsonPath("$[2].label").value("02hs"))
                .andExpect(jsonPath("$[3].label").value("03hs"))
                .andExpect(jsonPath("$[4].label").value("04hs"))
                .andExpect(jsonPath("$[5].label").value("05hs"))
                .andExpect(jsonPath("$[6].label").value("06hs"))
                .andExpect(jsonPath("$[7].label").value("07hs"))
                .andExpect(jsonPath("$[8].label").value("08hs"))
                .andExpect(jsonPath("$[9].label").value("09hs"))
                .andExpect(jsonPath("$[10].label").value("10hs"))
                .andExpect(jsonPath("$[11].label").value("11hs"))
                .andExpect(jsonPath("$[12].label").value("12hs"))
                .andExpect(jsonPath("$[13].label").value("13hs"))
                .andExpect(jsonPath("$[14].label").value("14hs"))
                .andExpect(jsonPath("$[15].label").value("15hs"))
                .andExpect(jsonPath("$[16].label").value("16hs"))
                .andExpect(jsonPath("$[17].label").value("17hs"))
                .andExpect(jsonPath("$[18].label").value("18hs"))
                .andExpect(jsonPath("$[19].label").value("19hs"))
                .andExpect(jsonPath("$[20].label").value("20hs"))
                .andExpect(jsonPath("$[21].label").value("21hs"))
                .andExpect(jsonPath("$[22].label").value("22hs"))
                .andExpect(jsonPath("$[23].label").value("23hs"))
                .andExpect(jsonPath("$[0].amount").value(0))
                .andExpect(jsonPath("$[1].amount").value(0))
                .andExpect(jsonPath("$[2].amount").value(0))
                .andExpect(jsonPath("$[3].amount").value(0))
                .andExpect(jsonPath("$[4].amount").value(0))
                .andExpect(jsonPath("$[5].amount").value(0))
                .andExpect(jsonPath("$[6].amount").value(0))
                .andExpect(jsonPath("$[7].amount").value(0))
                .andExpect(jsonPath("$[8].amount").value(0))
                .andExpect(jsonPath("$[9].amount").value(0))
                .andExpect(jsonPath("$[10].amount").value(0))
                .andExpect(jsonPath("$[11].amount").value(0))
                .andExpect(jsonPath("$[12].amount").value(0))
                .andExpect(jsonPath("$[13].amount").value(0))
                .andExpect(jsonPath("$[14].amount").value(0))
                .andExpect(jsonPath("$[15].amount").value(10))
                .andExpect(jsonPath("$[16].amount").value(0))
                .andExpect(jsonPath("$[17].amount").value(0))
                .andExpect(jsonPath("$[18].amount").value(0))
                .andExpect(jsonPath("$[19].amount").value(0))
                .andExpect(jsonPath("$[20].amount").value(0))
                .andExpect(jsonPath("$[21].amount").value(0))
                .andExpect(jsonPath("$[22].amount").value(0))
                .andExpect(jsonPath("$[23].amount").value(0))
                .andReturn();

        System.out.println("Response JSON: " + result.getResponse().getContentAsString());

        verify(dashboardService, times(1)).getPaymentsByTimeScale(timeScale);
    }

    @Test
    void testGetPaymentChartDataByWeek() throws Exception {
        String timeScale = "week";
        List<PaymentDTO> paymentDTOList = Collections.singletonList(
                PaymentDTO.builder()
                        .id(1L)
                        .lotId(2L)
                        .clientId(3L)
                        .clientName("Client 1")
                        .userId(4L)
                        .amount(BigDecimal.TEN)
                        .username("user1")
                        .file(null)
                        .createdDate(LocalDateTime.now().withHour(15).withMinute(10).withSecond(0).withNano(0))
                        .reason(PaymentReason.RESERVA)
                        .build()
        );

        when(dashboardService.getPaymentsByTimeScale(timeScale)).thenReturn(paymentDTOList);

        LocalDate today = LocalDate.now();
        mockMvc.perform(get("/dashboard/charts-info/{timeScale}", timeScale))
                .andExpect(status().isOk());

        verify(dashboardService, times(1)).getPaymentsByTimeScale(timeScale);
    }

    @Test
    void testGetPaymentChartDataByMonth() throws Exception {
        String timeScale = "month";
        List<PaymentDTO> paymentDTOList = Collections.singletonList(
                PaymentDTO.builder()
                        .id(1L)
                        .lotId(2L)
                        .clientId(3L)
                        .clientName("Client 1")
                        .userId(4L)
                        .amount(BigDecimal.TEN)
                        .username("user1")
                        .file(null)
                        .createdDate(LocalDateTime.now().withHour(15).withMinute(10).withSecond(0).withNano(0))
                        .reason(PaymentReason.RESERVA)
                        .build()
        );

        when(dashboardService.getPaymentsByTimeScale(timeScale)).thenReturn(paymentDTOList);

        MvcResult result = mockMvc.perform(get("/dashboard/charts-info/{timeScale}", timeScale))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("01"))
                .andReturn();

        System.out.println("Response JSON: " + result.getResponse().getContentAsString());

        verify(dashboardService, times(1)).getPaymentsByTimeScale(timeScale);
    }

    @Test
    void testGetPaymentChartDataByYear() throws Exception {
        String timeScale = "year";
        List<PaymentDTO> paymentDTOList = Collections.singletonList(
                PaymentDTO.builder()
                        .id(1L)
                        .lotId(2L)
                        .clientId(3L)
                        .clientName("Client 1")
                        .userId(4L)
                        .amount(BigDecimal.TEN)
                        .username("user1")
                        .file(null)
                        .createdDate(LocalDateTime.now().withHour(15).withMinute(10).withSecond(0).withNano(0))
                        .reason(PaymentReason.RESERVA)
                        .build()
        );

        when(dashboardService.getPaymentsByTimeScale(timeScale)).thenReturn(paymentDTOList);

        MvcResult result = mockMvc.perform(get("/dashboard/charts-info/{timeScale}", timeScale))
                .andExpect(status().isOk())
                // Ajusta los valores esperados según el año actual
                .andExpect(jsonPath("$[0].label").value("Jan"))
                .andExpect(jsonPath("$[1].label").value("Feb"))
                .andExpect(jsonPath("$[2].label").value("Mar"))
                .andExpect(jsonPath("$[3].label").value("Apr"))
                .andExpect(jsonPath("$[4].label").value("May"))
                .andExpect(jsonPath("$[5].label").value("Jun"))
                .andExpect(jsonPath("$[6].label").value("Jul"))
                .andExpect(jsonPath("$[7].label").value("Aug"))
                .andExpect(jsonPath("$[8].label").value("Sep"))
                .andExpect(jsonPath("$[9].label").value("Oct"))
                .andExpect(jsonPath("$[10].label").value("Nov"))
                .andExpect(jsonPath("$[11].label").value("Dec"))
                .andReturn();

        System.out.println("Response JSON: " + result.getResponse().getContentAsString());

        verify(dashboardService, times(1)).getPaymentsByTimeScale(timeScale);
    }

    @Test
    void testGetReservesForCalendar() throws Exception {
        // Configuración de datos de prueba
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2023-11-01");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2023-11-30");
        Long userId = 1L;

        List<IReserveCalendar> reserveCalendarList = Arrays.asList(
                new TestReserveCalendar("Reserve 1", new Date(), 1L, 2L),
                new TestReserveCalendar("Reserve 2", new Date(), 3L, 4L)
                // Agrega más instancias de TestReserveCalendar según sea necesario
        );

        // Configuración de Mock
        when(dashboardService.getReservesForCalendar(startDate, endDate, userId)).thenReturn(reserveCalendarList);

        // Ejecutar la solicitud y realizar afirmaciones
        mockMvc.perform(get("/dashboard/reserves-for-calendar")
                        .param("startDate", new SimpleDateFormat("yyyy-MM-dd").format(startDate))
                        .param("endDate", new SimpleDateFormat("yyyy-MM-dd").format(endDate))
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Reserve 1"))
                .andExpect(jsonPath("$[0].reserveId").value(1))
                .andExpect(jsonPath("$[0].lotId").value(2))
                .andExpect(jsonPath("$[1].title").value("Reserve 2"))
                .andExpect(jsonPath("$[1].reserveId").value(3))
                .andExpect(jsonPath("$[1].lotId").value(4));

        // Verificar que el servicio se llamó una vez con los parámetros correctos
        verify(dashboardService, times(1)).getReservesForCalendar(startDate, endDate, userId);
    }

    private static class TestReserveCalendar implements IReserveCalendar {
        private String title;
        private Date date;
        private Long reserveId;
        private Long lotId;

        public TestReserveCalendar(String title, Date date, Long reserveId, Long lotId) {
            this.title = title;
            this.date = date;
            this.reserveId = reserveId;
            this.lotId = lotId;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public Date getDate() {
            return date;
        }

        @Override
        public Long getReserveId() {
            return reserveId;
        }

        @Override
        public Long getLotId() {
            return lotId;
        }
    }

    @Test
    void testGetDashboardCardsInfo() throws Exception {
        String timeScale = "day";
        List<IDashboardCard> dashboardCardList = Collections.singletonList(
                new DummyDashboardCard("Title 1", "Guid 1")
        );

        when(dashboardService.getDashboardCardsInfo(timeScale)).thenReturn(dashboardCardList);

        mockMvc.perform(get("/dashboard/cards-info/{timeScale}", timeScale))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].guid").value("Guid 1"));

        verify(dashboardService, times(1)).getDashboardCardsInfo(timeScale);
    }

    private static class DummyDashboardCard implements IDashboardCard {
        private final String title;
        private final String guid;

        public DummyDashboardCard(String title, String guid) {
            this.title = title;
            this.guid = guid;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getGuid() {
            return guid;
        }
    }

}
