package com.api.landtracker.service;

import com.api.landtracker.model.dto.ReservaDTO;
import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.Reserva;
import com.api.landtracker.model.mappers.ReservaMapper;
import com.api.landtracker.model.mappers.ReservaMapperImpl;
import com.api.landtracker.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Spy
    private ReservaMapper reservaMapper = new ReservaMapperImpl();

    @Test
    public void testObtenerTodosLasReservas() {
        Cliente cliente1 = Cliente.builder().id(1L).nombre("Cliente 1").build();
        Cliente cliente2 = Cliente.builder().id(2L).nombre("Cliente 2").build();

        Lote lote1 = Lote.builder().id(1L).nombre("Lote 1").build();
        Lote lote2 = Lote.builder().id(2L).nombre("Lote 2").build();

        Reserva reserva1 = Reserva.builder().id(1L).cliente(cliente1).lote(lote1).build();
        Reserva reserva2 = Reserva.builder().id(2L).cliente(cliente2).lote(lote2).build();

        ReservaDTO reservaDTO1 = ReservaDTO.builder().id(1L).idCliente(cliente1.getId()).idLote(lote1.getId()).build();
        ReservaDTO reservaDTO2 = ReservaDTO.builder().id(2L).idCliente(cliente2.getId()).idLote(lote2.getId()).build();

        List<Reserva> reservas = Arrays.asList(reserva1, reserva2);
        List<ReservaDTO> reservaDTOs = Arrays.asList(reservaDTO1, reservaDTO2);

        when(reservaRepository.findAll()).thenReturn(reservas);

        List<ReservaDTO> resultado = reservaService.obtenerTodosLasReservas();

        assertEquals(reservaDTOs, resultado);
    }

    @Test
    public void testGuardarReserva() {
        Cliente cliente = Cliente.builder().id(1L).nombre("Cliente 1").build();
        Lote lote = Lote.builder().id(1L).nombre("Lote 1").build();

        Reserva reservaParaGuardar = Reserva.builder()
                .id(1L)
                .cliente(cliente)
                .lote(lote)
                .build();

        ReservaDTO reservaDTO = reservaMapper.reservaToReservaDTO(reservaParaGuardar);

        when(reservaMapper.reservaDTOToReserva(reservaDTO)).thenReturn(reservaParaGuardar);
        when(reservaMapper.reservaToReservaDTO(reservaParaGuardar)).thenReturn(reservaDTO);
        when(reservaRepository.save(reservaParaGuardar)).thenReturn(reservaParaGuardar);

        //when
        ReservaDTO resultado = reservaService.guardarReserva(reservaDTO);

        //verify
        verify(reservaRepository).save(reservaParaGuardar);
        assertEquals(reservaDTO, resultado);


    }
}