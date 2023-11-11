package com.api.landtracker.controller;

import com.api.landtracker.controller.ReserveController;
import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.ReserveState;
import com.api.landtracker.service.ReserveService;
import com.api.landtracker.utils.exception.DataValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
@ActiveProfiles("test")
public class ReserveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReserveController reserveController;

    @MockBean
    private ReserveService reserveService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reserveController).build();
    }

    @Test
    void testGetAllReserves() throws Exception {
        List<ReserveDTO> reserves = Arrays.asList(
                ReserveDTO.builder().id(1L).state(ReserveState.PENDIENTE_DE_PAGO).clientName("Client 1").build(),
                ReserveDTO.builder().id(2L).state(ReserveState.ABONADA).clientName("Client 2").build()
        );

        when(reserveService.getAllReserves()).thenReturn(reserves);

        mockMvc.perform(get("/reserves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(reserves.size())))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].state", is("PENDIENTE_DE_PAGO")))
                .andExpect(jsonPath("$[0].clientName", is("Client 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].state", is("ABONADA")))
                .andExpect(jsonPath("$[1].clientName", is("Client 2")));

        verify(reserveService, times(1)).getAllReserves();
    }

    @Test
    void testSaveReserve() throws Exception {
        ReserveDTO reserveDTO = ReserveDTO.builder()
                .id(1L)
                .state(ReserveState.PENDIENTE_DE_PAGO)
                .clientName("Client 1")
                .build();

        when(reserveService.saveReserve(any(ReserveDTO.class))).thenReturn(reserveDTO);

        mockMvc.perform(post("/reserves")
                        .contentType("application/json")
                        .content(asJsonString(reserveDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.state", is("PENDIENTE_DE_PAGO")))
                .andExpect(jsonPath("$.clientName", is("Client 1")));

        verify(reserveService, times(1)).saveReserve(any(ReserveDTO.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
