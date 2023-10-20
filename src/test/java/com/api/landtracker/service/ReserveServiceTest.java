package com.api.landtracker.service;

import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.model.mappers.ReserveMapperImpl;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReserveServiceTest {

    @InjectMocks
    private ReserveService reserveService;

    @Mock
    private ReserveRepository reserveRepository;
    @Mock
    private LotRepository lotRepository;

    @Spy
    private ReserveMapper reserveMapper = new ReserveMapperImpl();

    @Test
    public void testGetAllReserves() {
        Client client1 = Client.builder().id(1L).name("Cliente 1").build();
        Client client2 = Client.builder().id(2L).name("Cliente 2").build();

        Lot lot1 = Lot.builder().id(1L).name("Lote 1").build();
        Lot lot2 = Lot.builder().id(2L).name("Lote 2").build();

        Reserve reserve1 = Reserve.builder().id(1L).client(client1).lot(lot1).build();
        Reserve reserve2 = Reserve.builder().id(2L).client(client2).lot(lot2).build();

        ReserveDTO reserveDTO1 = ReserveDTO.builder().id(1L)
                .clientId(client1.getId())
                .clientName(client1.getName())
                .lotId(lot1.getId())
                .lotName(lot1.getName())
                .build();
        ReserveDTO reserveDTO2 = ReserveDTO.builder().id(2L)
                .clientId(client2.getId())
                .clientName(client2.getName())
                .lotId(lot2.getId())
                .lotName(lot2.getName())
                .build();

        List<Reserve> reserves = Arrays.asList(reserve1, reserve2);
        List<ReserveDTO> reservesDTO = Arrays.asList(reserveDTO1, reserveDTO2);

        when(reserveRepository.findAll()).thenReturn(reserves);

        List<ReserveDTO> response = reserveService.getAllReserves();

        assertEquals(reservesDTO, response);
    }

    @Test
    public void testSaveReserve() throws DataValidationException {
        Client client = Client.builder().id(1L).name("Cliente 1").build();
        Lot lot = Lot.builder().id(1L).name("Lote 1").build();

        Reserve reserveToSave = Reserve.builder()
                .id(1L)
                .client(client)
                .lot(lot)
                .build();

        ReserveDTO reserveDTO = reserveMapper.reserveToReserveDTO(reserveToSave);

        when(reserveMapper.reserveDTOToReserve(reserveDTO)).thenReturn(reserveToSave);
        when(reserveMapper.reserveToReserveDTO(reserveToSave)).thenReturn(reserveDTO);
        when(reserveRepository.save(reserveToSave)).thenReturn(reserveToSave);
        when(lotRepository.findById(1L)).thenReturn(Optional.ofNullable(lot));
        when(lotRepository.save(lot)).thenReturn(lot);

        //when
        ReserveDTO response = reserveService.saveReserve(reserveDTO);

        //verify
        verify(reserveRepository).save(reserveToSave);
        assertEquals(reserveDTO, response);


    }
}