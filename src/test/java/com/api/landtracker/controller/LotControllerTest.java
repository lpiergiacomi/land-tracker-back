package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.LotPositionDTO;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.service.LotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.sql.init.mode=never"})
@ActiveProfiles("test")
public class LotControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public LotController lotController;

    @MockBean
    public LotService lotService;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(lotController).build();
    }

    @Test
    void getAllLotsTest() throws Exception {
        List<LotDTO> lots = new ArrayList<>();
        LotDTO lot = LotDTO.builder()
                .id(1L)
                .state(LotState.DISPONIBLE)
                .name("Un lote")
                .area(1000)
                .position(new LotPositionDTO(1.0,2.0,3.0))
                .build();
        lots.add(lot);

        when(lotService.getAllLots()).thenReturn(lots);

        //when
        mockMvc.perform(get("/lots"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(lots.size())));
    }

    @Test
    void getLotByIdTest() throws Exception {
        LotDTO lot = LotDTO.builder()
                .id(1L)
                .state(LotState.DISPONIBLE)
                .name("Un lote")
                .area(1000)
                .position(new LotPositionDTO(1.0, 2.0, 3.0))
                .build();

        when(lotService.getLotById(1L)).thenReturn(lot);

        mockMvc.perform(get("/lots/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.state").value("DISPONIBLE"))
                .andExpect(jsonPath("$.name").value("Un lote"))
                .andExpect(jsonPath("$.area").value(1000))
                .andExpect(jsonPath("$.position.x").value(1.0))
                .andExpect(jsonPath("$.position.y").value(2.0))
                .andExpect(jsonPath("$.position.z").value(3.0));

        verify(lotService, times(1)).getLotById(1L);
    }
}