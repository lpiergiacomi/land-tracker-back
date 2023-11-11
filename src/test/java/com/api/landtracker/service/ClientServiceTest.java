package com.api.landtracker.service;

import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.filter.ClientFilterParams;
import com.api.landtracker.repository.ClientRepository;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import com.api.landtracker.utils.exception.RecordNotFoundHttpException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    ClientRepository clientRepository;
    @Mock
    private LotRepository lotRepository;

    @Mock
    private ReserveRepository reserveRepository;

    @Test
    void saveClient() throws DataValidationException {

        Client client = new Client();

        when(clientRepository.save(client)).thenReturn(client);

        //when
        Client result = clientService.saveClient(client);

        //verify
        verify(clientRepository, times(1)).save(client);
        assertSame(client, result);

    }

    @Test
    void testSaveClientDataIntegrityException() {
        Client client = new Client();

        when(clientRepository.save(client)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataValidationException.class, () -> clientService.saveClient(client));

        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void getAllClients() {

        Client client1 = Client.builder()
                .id(1L)
                .name("Cliente test")
                .build();
        Client client2 = Client.builder()
                .id(1L)
                .name("Cliente test")
                .build();
        List<Client> clients = new ArrayList<>(List.of(client1, client2));

        when(clientRepository.findAll()).thenReturn(clients);

        //when
        List<Client> clientsResponse = clientService.getAllClients();

        //verify

        verify(clientRepository).findAll();
        assertAll(() -> {
            assertEquals(clientsResponse.size(), clients.size());
        });
    }

    @Test
    void getAllClientsWithFilter() {

        ClientFilterParams filterParams = new ClientFilterParams();
        filterParams.setName("Cliente test");

        Client client1 = Client.builder()
                .id(1L)
                .name("Cliente test 1")
                .build();
        Client client2 = Client.builder()
                .id(2L)
                .name("Cliente test 2")
                .build();
        List<Client> filteredClients = new ArrayList<>(List.of(client1, client2));

        Page<Client> pageMock = new PageImpl<>(filteredClients);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageMock);

        //when
        Page<Client> clientsResponse = clientService.getAllClientsWithFilter(filterParams, Pageable.unpaged());

        ArgumentCaptor specificationCaptor = ArgumentCaptor.forClass(Specification.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        //verify
        verify(clientRepository).findAll((Specification<Client>) specificationCaptor.capture(), pageableCaptor.capture());
        assertEquals(pageMock, clientsResponse);
        assertEquals(filteredClients, clientsResponse.getContent());

    }

    @Test
    void testDeleteClient() throws DataValidationException {
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(lotRepository.findLotsByClientId(clientId)).thenReturn(List.of());
        when(reserveRepository.findReservesByClientId(clientId)).thenReturn(List.of());

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void testDeleteClientWithDataException() {
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(lotRepository.findLotsByClientId(clientId)).thenReturn(List.of(new Lot())); // Simulate having associated lots

        assertThrows(DataValidationException.class, () -> clientService.deleteClient(clientId));

        verify(clientRepository, never()).deleteById(clientId);
    }

    @Test
    void testDeleteClientNotFound() {
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundHttpException.class, () -> clientService.deleteClient(clientId));

        verify(clientRepository, never()).deleteById(clientId);
    }
}