package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.entities.LotPosition;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.entities.Lot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LotMapperTest {

    private static LotMapper lotMapper;

    @BeforeAll
    public static void setUp(){
        lotMapper = new LotMapperImpl();
    }
    @Test
    public void testLotToLotDTO() {
        Lot lot = Lot.builder()
                .id(1L)
                .state(LotState.DISPONIBLE)
                .name("Un lote")
                .area(1000)
                .position(new LotPosition(1.0,2.0,3.0))
                .build();

        LotDTO lotDTO = lotMapper.lotToLotDTO(lot);

        assertAll(
                () -> {
                    assertEquals("Un lote", lotDTO.getName());
                    assertEquals(1000, lotDTO.getArea());
                    assertEquals(LotState.DISPONIBLE, lotDTO.getState());
                    assertEquals(lot.getPosition().getX(), lotDTO.getPosition().getX());
                    assertEquals(lot.getPosition().getY(), lotDTO.getPosition().getY());
                    assertEquals(lot.getPosition().getZ(), lotDTO.getPosition().getZ());
                }
        );
    }

    @Test
    public void testLotDTOToLot() {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setName("Lote B");
        lotDTO.setArea(150);
        lotDTO.setState(LotState.RESERVADO);

        Lot lot = lotMapper.lotDTOToLot(lotDTO);

        assertNotNull(lot);
        assertEquals("Lote B", lot.getName());
        assertEquals(150, lot.getArea());
        assertEquals(LotState.RESERVADO, lot.getState());
    }
}
