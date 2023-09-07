package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.dto.PosicionLoteDTO;
import com.api.landtracker.model.entities.EstadoLote;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.PosicionLote;
import com.api.landtracker.service.LoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.sql.init.mode=never"})
@ActiveProfiles("test")
public class LoteControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public LoteController loteController;

    @MockBean
    public LoteService loteService;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(loteController).build();
    }

    @Test
    void obtenerTodosLosLotesTest() throws Exception {
        List<LoteDTO> lotes = new ArrayList<>();
        LoteDTO lote = LoteDTO.builder()
                .id(1L)
                .estadoLote(EstadoLote.DISPONIBLE)
                .nombre("Un lote")
                .superficie(1000)
                .posicionLote(new PosicionLoteDTO(1.0,2.0,3.0))
                .build();
        lotes.add(lote);

        when(loteService.obtenerTodosLosLotes()).thenReturn(lotes);

        //when
        mockMvc.perform(get("/api/lotes"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(lotes.size())));
    }
}