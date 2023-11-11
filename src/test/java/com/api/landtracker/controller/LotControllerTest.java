package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.LotPositionDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.service.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
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

    @Test
    void saveLotTest() throws Exception {
        LotDTO lot = LotDTO.builder()
                .id(1L)
                .state(LotState.DISPONIBLE)
                .name("Nuevo Lote")
                .area(1200)
                .position(new LotPositionDTO(1.0, 2.0, 3.0))
                .build();

        when(lotService.saveLot(any())).thenReturn(lot);

        mockMvc.perform(post("/lots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.state").value("DISPONIBLE"))
                .andExpect(jsonPath("$.name").value("Nuevo Lote"))
                .andExpect(jsonPath("$.area").value(1200))
                .andExpect(jsonPath("$.position.x").value(1.0))
                .andExpect(jsonPath("$.position.y").value(2.0))
                .andExpect(jsonPath("$.position.z").value(3.0));

        verify(lotService, times(1)).saveLot(any());
    }

    @Test
    void getFilteredLotsTest() throws Exception {
        LotFilterParams lotParams = new LotFilterParams();
        lotParams.setStates(Collections.singletonList(LotState.DISPONIBLE));

        List<LotDTO> filteredLots = Arrays.asList(
                LotDTO.builder().id(1L).state(LotState.DISPONIBLE).name("Lote 1").build(),
                LotDTO.builder().id(2L).state(LotState.DISPONIBLE).name("Lote 2").build()
        );

        when(lotService.getAllLotsWithFilter(any())).thenReturn(filteredLots);

        mockMvc.perform(post("/lots/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(lotParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(filteredLots.size())));

        verify(lotService, times(1)).getAllLotsWithFilter(any());
    }

    @Test
    void updateAssignedLotsToUserTest() throws Exception {
        // Arrange
        UserWithAssignedLotsDTO userDTO = new UserWithAssignedLotsDTO();
        userDTO.setId(1L);
        userDTO.setUsername("john.doe");
        userDTO.setAssignedLotsIds(Arrays.asList(1L, 2L, 3L));

        when(lotService.updateAssignedLotsToUser(any())).thenReturn(userDTO);

        // Act and Assert
        mockMvc.perform(post("/lots/update-assigned-lots-to-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john.doe"))
                .andExpect(jsonPath("$.assignedLotsIds.length()", is(userDTO.getAssignedLotsIds().size())));

        verify(lotService, times(1)).updateAssignedLotsToUser(any());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}