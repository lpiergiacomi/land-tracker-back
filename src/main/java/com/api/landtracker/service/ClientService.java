package com.api.landtracker.service;

import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.Lot;
import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.filter.ClientFilterParams;
import com.api.landtracker.model.filter.ClientSpecification;
import com.api.landtracker.repository.ClientRepository;
import com.api.landtracker.repository.LotRepository;
import com.api.landtracker.repository.ReserveRepository;
import com.api.landtracker.utils.exception.DataValidationException;
import com.api.landtracker.utils.exception.RecordNotFoundHttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final LotRepository lotRepository;
    private final ReserveRepository reserveRepository;

    @Transactional(rollbackOn = Exception.class)
    public Client saveClient(Client client) throws DataValidationException {
        try {
            clientRepository.save(client);
            return client;
        } catch (DataIntegrityViolationException exception) {
            throw new DataValidationException("Ya existe un cliente con ese email");
        }
    }

    public List<Client> getAllClients() {
        return this.clientRepository.findAll();
    }

    public Page<Client> getAllClientsWithFilter(ClientFilterParams params, Pageable pageable) {

        Specification<Client> nameLike = ClientSpecification.clientNameLike(params.getName());
        Specification<Client> emailLike = ClientSpecification.emailLike(params.getName());
        Specification<Client> documentLike = ClientSpecification.documentLike(params.getName());

        Page<Client> clientPage = this.clientRepository.findAll(
                Specification.where(nameLike).or(documentLike).or(emailLike),
                pageable);

        return new PageImpl<Client>(clientPage.getContent(), pageable, clientPage.getTotalElements());
    }

    public void deleteClient(Long id) throws DataValidationException {

        Client client = clientRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundHttpException("No existe un cliente con Id: " + id));
        List<Lot> lots = lotRepository.findLotsByClientId(id);
        List<Reserve> reserves = reserveRepository.findReservesByClientId(id);
        if (!lots.isEmpty() || !reserves.isEmpty()){
            throw new DataValidationException("No es posible eliminar. Existen lotes o reservas relacionadas con el cliente que desea eliminar");
        } else {
            clientRepository.deleteById(id);
        }
    }
}
