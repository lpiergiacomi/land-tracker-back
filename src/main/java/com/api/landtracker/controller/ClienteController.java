package com.api.landtracker.controller;

import com.api.landtracker.model.entities.Cliente;
import com.api.landtracker.model.filter.ClienteFilterParams;
import com.api.landtracker.service.ClienteService;
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
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Cliente>> pages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "200") int size,
            @RequestParam(defaultValue = "nombre") String order,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestBody ClienteFilterParams clienteParams) {

        Page<Cliente> clientes = clienteService.obtenerClientesConFiltro(clienteParams,
                PageRequest.of(page, size, Sort.by(order)));
        return ok(clientes);
    }
    @PostMapping
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarCliente(@PathVariable Long id) throws DataValidationException {
        clienteService.eliminarCliente(id);
        ApiResponse<String> response = new ApiResponse("success", "ok", "");
        return ok(response);
    }

}
