package com.api.landtracker.service;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.LotPosition;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.mappers.LotMapper;
import com.api.landtracker.model.mappers.LotMapperImpl;
import com.api.landtracker.repository.LotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotServiceTest {


    @InjectMocks
    private LotService lotService;

    @Mock
    LotRepository lotRepository;

    @Spy
    private LotMapper lotMapper = new LotMapperImpl();

    @Test
    void getAllLots() {
        List<Lot> lots = new ArrayList<>();
        Lot lot = Lot.builder()
                .id(1L)
                .state(LotState.DISPONIBLE)
                .name("Un lote")
                .area(1000)
                .position(new LotPosition(1.0,2.0,3.0))
                .build();
        lots.add(lot);

        when(lotRepository.findAll()).thenReturn(lots);

        //when
        List<LotDTO> lotResponse = lotService.getAllLots();

        //verify

        verify(lotRepository).findAll();
        assertAll(() -> {
            assertEquals(lotResponse.size(), lots.size());
        });

    }

    @Test
    void testSaveLot() {
        LotDTO lotDTO = new LotDTO();
        Lot lot = new Lot();
        when(lotMapper.lotDTOToLot(lotDTO)).thenReturn(lot);
        when(lotMapper.lotToLotDTO(lot)).thenReturn(lotDTO);

        when(lotRepository.save(lot)).thenReturn(lot);

        //when
        LotDTO response = lotService.saveLot(lotDTO);

        //verify
        verify(lotRepository, times(1)).save(lot);
        assertSame(lotDTO, response);
    }

    @Test
    void testGetLotById() {
        Long id = 1L;

        Lot lot = Lot.builder()
                .id(1L)
                .state(LotState.DISPONIBLE)
                .name("Un lote")
                .area(1000)
                .position(new LotPosition(1.0,2.0,3.0))
                .build();
        when(lotRepository.findById(id)).thenReturn(Optional.of(lot));

        //when
        LotDTO response = lotService.getLotById(id);

        //verify
        verify(lotRepository, times(1)).findById(id);
        assertEquals(lot.getId(), response.getId());
        assertEquals(lot.getState(), response.getState());
        assertEquals(lot.getMetersBack(), response.getMetersBack());
        assertEquals(lot.getMetersFront(), response.getMetersFront());
        assertEquals(lot.getArea(), response.getArea());
        assertEquals(lot.getPrice(), response.getPrice());
        assertEquals(lot.getMunicipalAccNumber(), response.getMunicipalAccNumber());
        assertEquals(lot.getName(), response.getName());
    }

    @Test
    void testGetLotById_ThrowsRuntimeException() {
        Long id = 1L;

        when(lotRepository.findById(id)).thenReturn(Optional.empty());

        try {
            lotService.getLotById(id);
            fail("Se esperaba una RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e instanceof RuntimeException);
        }

        verify(lotRepository, times(1)).findById(id);
    }
}