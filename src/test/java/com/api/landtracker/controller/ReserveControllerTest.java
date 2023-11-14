package com.api.landtracker.controller;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.ReserveState;
import com.api.landtracker.service.ReserveService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
