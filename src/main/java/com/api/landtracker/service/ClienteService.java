package com.api.landtracker.service;

import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente guardarCliente(Cliente cliente){
        clienteRepository.save(cliente);
        return cliente;
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return this.clienteRepository.findAll();
    }
}
