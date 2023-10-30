package com.api.landtracker.controller;

import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.filter.ClientFilterParams;
import com.api.landtracker.service.ClientService;
import com.api.landtracker.utils.ApiResponse;
import com.api.landtracker.utils.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Client>> pages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            @RequestParam(defaultValue = "name") String order,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestBody ClientFilterParams clientParams) {

        Page<Client> clients = clientService.getAllClientsWithFilter(clientParams,
                PageRequest.of(page, size, Sort.by(order)));
        return ok(clients);
    }
    @PostMapping
    public Client saveClient(@RequestBody Client client) throws DataValidationException {
        return clientService.saveClient(client);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteClient(@PathVariable Long id) throws DataValidationException {
        clientService.deleteClient(id);
        ApiResponse<String> response = new ApiResponse<>("success", "ok", "");
        return ok(response);
    }

}
