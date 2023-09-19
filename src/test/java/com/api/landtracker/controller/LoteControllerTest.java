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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        mockMvc.perform(get("/lotes"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(lotes.size())));
    }

    @Test
    void obtenerLotePorIdTest() throws Exception {
        LoteDTO lote = LoteDTO.builder()
                .id(1L)
                .estadoLote(EstadoLote.DISPONIBLE)
                .nombre("Un lote")
                .superficie(1000)
                .posicionLote(new PosicionLoteDTO(1.0, 2.0, 3.0))
                .build();

        when(loteService.obtenerLotePorId(1L)).thenReturn(lote);

        mockMvc.perform(get("/lotes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estadoLote").value("DISPONIBLE"))
                .andExpect(jsonPath("$.nombre").value("Un lote"))
                .andExpect(jsonPath("$.superficie").value(1000))
                .andExpect(jsonPath("$.posicionLote.x").value(1.0))
                .andExpect(jsonPath("$.posicionLote.y").value(2.0))
                .andExpect(jsonPath("$.posicionLote.z").value(3.0));

        verify(loteService, times(1)).obtenerLotePorId(1L);
    }
}