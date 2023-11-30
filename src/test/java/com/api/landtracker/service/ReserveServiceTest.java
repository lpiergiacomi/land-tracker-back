package com.api.landtracker.service;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.ReserveDTO;
import com.api.landtracker.model.entities.*;
import com.api.landtracker.model.mappers.LotMapper;
import com.api.landtracker.model.mappers.LotMapperImpl;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.model.mappers.ReserveMapperImpl;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.repository.UserRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
    @Mock
    private UserRepository userRepository;
    @Spy
    private ReserveMapper reserveMapper = new ReserveMapperImpl();
    @Spy
    private LotMapper lotMapper = new LotMapperImpl();

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
        Lot lot = Lot.builder().id(1L).name("Lote 1").state(LotState.DISPONIBLE).build();
        User user = new User();
        user.setId(1L);
        user.setAssignedLots(Collections.singletonList(lot));

        Reserve reserveToSave = Reserve.builder()
                .id(1L)
                .client(client)
                .lot(lot)
                .user(user)
                .build();

        ReserveDTO reserveDTO = reserveMapper.reserveToReserveDTO(reserveToSave);

        when(reserveMapper.reserveDTOToReserve(reserveDTO)).thenReturn(reserveToSave);
        when(reserveMapper.reserveToReserveDTO(reserveToSave)).thenReturn(reserveDTO);
        when(reserveRepository.save(reserveToSave)).thenReturn(reserveToSave);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //when
        ReserveDTO response = reserveService.saveReserve(reserveDTO);

        //verify
        verify(reserveRepository).save(reserveToSave);
        assertEquals(reserveDTO, response);
    }

    @Test
    public void testUpdateDueDate() throws DataValidationException {
        Client client = Client.builder().id(1L).name("Cliente 1").build();
        Lot lot = Lot.builder().id(1L).name("Lote 1").state(LotState.DISPONIBLE).build();
        User user = new User();
        user.setId(1L);
        user.setAssignedLots(Collections.singletonList(lot));

        Reserve reserveToSave = Reserve.builder()
                .id(1L)
                .dueDate(LocalDate.now())
                .client(client)
                .lot(lot)
                .user(user)
                .build();

        ReserveDTO reserveDTO = reserveMapper.reserveToReserveDTO(reserveToSave);

        when(reserveMapper.reserveToReserveDTO(reserveToSave)).thenReturn(reserveDTO);
        when(reserveRepository.save(reserveToSave)).thenReturn(reserveToSave);
        when(reserveRepository.findById(1L)).thenReturn(Optional.of(reserveToSave));
        when(lotRepository.findAssignmentLotByUserId(1L, 1L)).thenReturn(Optional.of(1L));
        //when
        ReserveDTO response = reserveService.updateDueDate(1L, new Date(), 1L, 1L );

        //verify
        verify(reserveRepository).save(reserveToSave);
        assertEquals(reserveDTO.getDueDate(), response.getDueDate());
    }

    @Test
    void testUpdateDueDate_ThrowsDataValidationException() {
        Long id = 1L;
        when(reserveRepository.findById(id)).thenReturn(Optional.empty());

        try {
            reserveService.updateDueDate(1L, new Date(),1L,1L);
            fail("Se esperaba una DataValidationException");
        } catch (DataValidationException e) {
            assertTrue(true);
        }

        verify(reserveRepository, times(1)).findById(id);
    }

    @Test
    void testCancel() throws DataValidationException {
        Long reserveId = 1L;
        Long lotId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        Lot lot = Lot.builder().id(lotId).state(LotState.RESERVADO).build();
        Reserve reserve = Reserve.builder().id(reserveId).user(user).lot(lot).state(ReserveState.PENDIENTE_DE_PAGO).build();

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.of(reserve));
        when(lotRepository.findAssignmentLotByUserId(1L, 1L)).thenReturn(Optional.of(1L));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));
        when(lotRepository.save(lot)).thenReturn(lot);

        LotDTO result = reserveService.cancel(reserveId, lotId, userId);

        verify(reserveRepository, times(1)).findById(reserveId);
        verify(lotRepository, times(1)).findById(lotId);
        verify(lotRepository, times(1)).save(lot);

        assertEquals(LotState.DISPONIBLE, result.getState());
    }

    @Test
    void testCancel_ReserveNotFound() {
        Long reserveId = 1L;
        Long lotId = 1L;
        Long userId = 1L;

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.empty());

        assertThrows(DataValidationException.class, () -> reserveService.cancel(reserveId, lotId, userId));

        verify(reserveRepository, times(1)).findById(reserveId);
        verify(lotRepository, never()).findById(anyLong());
        verify(lotRepository, never()).save(any(Lot.class));
    }

    @Test
    void testCancel_LotNotFound() {
        Long reserveId = 1L;
        Long lotId = 1L;
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        when(reserveRepository.findById(reserveId)).thenReturn(Optional.of(new Reserve()));
        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        assertThrows(DataValidationException.class, () -> reserveService.cancel(reserveId, lotId, userId));

        verify(reserveRepository, times(1)).findById(reserveId);
        verify(lotRepository, times(1)).findById(lotId);
        verify(lotRepository, never()).save(any(Lot.class));
    }
}