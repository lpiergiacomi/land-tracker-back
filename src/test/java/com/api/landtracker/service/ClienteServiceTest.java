package com.api.landtracker.service;

import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.filter.ClienteFilterParams;
import com.api.landtracker.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    @Test
    void guardarCliente() {

        Cliente cliente = new Cliente();

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        //when
        Cliente resultado = clienteService.guardarCliente(cliente);

        //verify
        verify(clienteRepository, times(1)).save(cliente);
        assertSame(cliente, resultado);

    }

    @Test
    void obtenerTodosLosClientes() {

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Cliente test")
                .build();
        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Cliente test")
                .build();
        List<Cliente> clientes = new ArrayList<>(List.of(cliente1, cliente2));

        when(clienteRepository.findAll()).thenReturn(clientes);

        //when
        List<Cliente> clientesResponse = clienteService.obtenerTodosLosClientes();

        //verify

        verify(clienteRepository).findAll();
        assertAll(() -> {
            assertEquals(clientesResponse.size(), clientes.size());
        });
    }

    @Test
    void obtenerClientesConFiltro() {

        ClienteFilterParams filterParams = new ClienteFilterParams();
        filterParams.setNombre("Cliente test");

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Cliente test 1")
                .build();
        Cliente cliente2 = Cliente.builder()
                .id(2L)
                .nombre("Cliente test 2")
                .build();
        List<Cliente> clientesFiltrados = new ArrayList<>(List.of(cliente1, cliente2));

        Page<Cliente> pageMock = new PageImpl<>(clientesFiltrados);

        when(clienteRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageMock);

        //when
        Page<Cliente> clientesResponse = clienteService.obtenerClientesConFiltro(filterParams, Pageable.unpaged());

        ArgumentCaptor specificationCaptor = ArgumentCaptor.forClass(Specification.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        //verify
        verify(clienteRepository).findAll((Specification<Cliente>) specificationCaptor.capture(), pageableCaptor.capture());
        assertEquals(pageMock, clientesResponse);
        assertEquals(clientesFiltrados, clientesResponse.getContent());

    }
}