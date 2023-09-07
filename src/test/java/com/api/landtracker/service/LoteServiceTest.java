package com.api.landtracker.service;

import com.api.landtracker.model.dto.LoteDTO;
import com.api.landtracker.model.entities.EstadoLote;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.PosicionLote;
import com.api.landtracker.model.mappers.LoteMapper;
import com.api.landtracker.model.mappers.LoteMapperImpl;
import com.api.landtracker.repository.LoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {


    @InjectMocks
    private LoteService loteService;

    @Mock
    LoteRepository loteRepository;

    @Spy
    private LoteMapper loteMapper = new LoteMapperImpl();

    @Test
    void obtenerTodosLosLotes() {
        List<Lote> lotes = new ArrayList<>();
        Lote lote = Lote.builder()
                .id(1L)
                .estadoLote(EstadoLote.DISPONIBLE)
                .nombre("Un lote")
                .superficie(1000)
                .posicionLote(new PosicionLote(1.0,2.0,3.0))
                .build();
        lotes.add(lote);

        Mockito.when(loteRepository.findAll()).thenReturn(lotes);

        //when
        List<LoteDTO> respLotes = loteService.obtenerTodosLosLotes();

        //verify

        verify(loteRepository).findAll();
        assertAll(() -> {
            assertEquals(respLotes.size(), lotes.size());
        });

    }
}