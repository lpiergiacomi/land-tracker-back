package com.api.landtracker.service;

import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.filter.ClienteFilterParams;
import com.api.landtracker.model.filter.ClienteSpecification;
import com.api.landtracker.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<Cliente> obtenerClientesConFiltro(ClienteFilterParams params, Pageable pageable) {

        Specification<Cliente> nombreLike = ClienteSpecification.clienteNombreLike(params.getNombre());
        Specification<Cliente> emailLike = ClienteSpecification.emailLike(params.getNombre());
        Specification<Cliente> documentoLike = ClienteSpecification.documentoLike(params.getNombre());

        Page<Cliente> clientePage = this.clienteRepository.findAll(
                Specification.where(nombreLike).or(documentoLike).or(emailLike),
                pageable);

        return new PageImpl<Cliente>(clientePage.getContent(), pageable, clientePage.getTotalElements());
    }
}
