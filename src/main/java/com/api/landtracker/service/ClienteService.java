package com.api.landtracker.service;

import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.Reserva;
import com.api.landtracker.model.filter.ClienteFilterParams;
import com.api.landtracker.model.filter.ClienteSpecification;
import com.api.landtracker.repository.ClienteRepository;
import com.api.landtracker.repository.LoteRepository;
import com.api.landtracker.repository.ReservaRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import com.api.landtracker.utils.exception.RecordNotFoundHttpException;
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
    private final LoteRepository loteRepository;
    private final ReservaRepository reservaRepository;

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

    public void eliminarCliente(Long id) throws DataValidationException {

        Cliente cliente = clienteRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundHttpException("No existe un cliente con Id: " + id));
        List<Lote> lotes = loteRepository.findLotesByClienteId(id);
        List<Reserva> reservas = reservaRepository.findReservasByClienteId(id);
        if (!lotes.isEmpty() || !reservas.isEmpty()){
            throw new DataValidationException("No es posible eliminar. Existen lotes o reservas relacionadas con el cliente que desea eliminar");
        } else {
            clienteRepository.deleteById(id);
        }
    }
}
