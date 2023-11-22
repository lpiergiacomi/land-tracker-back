package com.api.landtracker.service;

import com.api.landtracker.model.dto.IPieChartData;
import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.LotPosition;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.model.mappers.LotMapper;
import com.api.landtracker.model.mappers.LotMapperImpl;
import com.api.landtracker.model.mappers.PaymentMapper;
import com.api.landtracker.model.mappers.ReserveMapper;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.PaymentRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.repository.UserRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotServiceTest {


    @InjectMocks
    private LotService lotService;

    @Mock
    private LotRepository lotRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private ReserveRepository reserveRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReserveMapper reserveMapper;

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
    void testGetAllLotsWithFilter() {
        // Mocking
        LotFilterParams params = new LotFilterParams();
        Page<Lot> page = new PageImpl<>(Arrays.asList(
                new Lot(), new Lot(), new Lot()
        ));

        when(lotRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Testing
        List<LotDTO> result = lotService.getAllLotsWithFilter(params);

        // Verification
        verify(lotRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        assertEquals(3, result.size());
    }

    @Test
    void testUpdateAssignedLotsToUser() throws DataValidationException {
        // Mocking
        UserWithAssignedLotsDTO user = new UserWithAssignedLotsDTO();
        user.setId(1L);
        user.setAssignedLotsIds(Arrays.asList(2L, 3L));

        Lot lot1 = new Lot();
        lot1.setId(2L);
        Lot lot2 = new Lot();
        lot2.setId(3L);

        when(lotRepository.findAllByIdIn(user.getAssignedLotsIds())).thenReturn(Arrays.asList(lot1, lot2));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(new User()));

        // Testing
        UserWithAssignedLotsDTO result = lotService.updateAssignedLotsToUser(user);

        // Verification
        verify(lotRepository, times(1)).deleteAssignedLotsByUserId(user.getId(), user.getAssignedLotsIds(), false);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(user.getAssignedLotsIds(), result.getAssignedLotsIds());
    }

    @Test
    void testGetLotsQuantityByState() {
        // Mocking
        List<IPieChartData> pieChartDataList = Arrays.asList(
                new PieChartDataImpl("DISPONIBLE", 5.0),
                new PieChartDataImpl("RESERVADO", 3.0),
                new PieChartDataImpl("VENDIDO", 7.0)
        );

        when(lotRepository.getLotsQuantityByState()).thenReturn(pieChartDataList);

        // Testing
        List<IPieChartData> result = lotService.getLotsQuantityByState();

        // Verification
        verify(lotRepository, times(1)).getLotsQuantityByState();
        assertEquals(pieChartDataList, result);
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

    class PieChartDataImpl implements IPieChartData {

        private final String label;
        private final Double data;

        public PieChartDataImpl(String label, Double data) {
            this.label = label;
            this.data = data;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public Double getData() {
            return data;
        }
    }
}