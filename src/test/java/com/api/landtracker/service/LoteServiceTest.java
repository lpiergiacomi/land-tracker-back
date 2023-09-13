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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(loteRepository.findAll()).thenReturn(lotes);

        //when
        List<LoteDTO> respLotes = loteService.obtenerTodosLosLotes();

        //verify

        verify(loteRepository).findAll();
        assertAll(() -> {
            assertEquals(respLotes.size(), lotes.size());
        });

    }

    @Test
    void testGuardarLote() {
        LoteDTO loteDTO = new LoteDTO();
        Lote lote = new Lote();
        when(loteMapper.loteDTOToLote(loteDTO)).thenReturn(lote);
        when(loteMapper.loteToLoteDTO(lote)).thenReturn(loteDTO);

        when(loteRepository.save(lote)).thenReturn(lote);

        //when
        LoteDTO resultado = loteService.guardarLote(loteDTO);

        //verify
        verify(loteRepository, times(1)).save(lote);
        assertSame(loteDTO, resultado);
    }

    @Test
    void testObtenerLotePorId() {
        Long id = 1L;

        Lote lote = Lote.builder()
                .id(1L)
                .estadoLote(EstadoLote.DISPONIBLE)
                .nombre("Un lote")
                .superficie(1000)
                .posicionLote(new PosicionLote(1.0,2.0,3.0))
                .build();
        when(loteRepository.findById(id)).thenReturn(Optional.of(lote));

        //when
        LoteDTO resultado = loteService.obtenerLotePorId(id);

        //verify
        verify(loteRepository, times(1)).findById(id);
        assertEquals(lote.getId(), resultado.getId());
        assertEquals(lote.getEstadoLote(), resultado.getEstadoLote());
        assertEquals(lote.getMetrosFondo(), resultado.getMetrosFondo());
        assertEquals(lote.getMetrosFrente(), resultado.getMetrosFrente());
        assertEquals(lote.getSuperficie(), resultado.getSuperficie());
        assertEquals(lote.getPrecio(), resultado.getPrecio());
        assertEquals(lote.getNroCuentaMunicipal(), resultado.getNroCuentaMunicipal());
        assertEquals(lote.getNombre(), resultado.getNombre());
    }

    @Test
    void testObtenerLotePorId_LanzaRuntimeException() {
        Long id = 1L;

        when(loteRepository.findById(id)).thenReturn(Optional.empty());

        try {
            loteService.obtenerLotePorId(id);
            fail("Se esperaba una RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e instanceof RuntimeException);
        }

        verify(loteRepository, times(1)).findById(id);
    }
}