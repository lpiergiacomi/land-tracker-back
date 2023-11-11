package com.api.landtracker.controller;

import com.api.landtracker.model.dto.LotDTO;
import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.entities.LotState;
import com.api.landtracker.model.filter.ClientFilterParams;
import com.api.landtracker.model.filter.LotFilterParams;
import com.api.landtracker.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest()
@ActiveProfiles("test")
class ClientControllerTest {

    public MockMvc mockMvc;

    @Autowired
    public ClientController clientController;

    @MockBean
    public ClientService clientService;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void testGetAllClients() throws Exception {
        List<Client> clients = Arrays.asList(
            Client.builder().id(1L).name("Lionel Messi").email("lionel.messi@example.com").build(),
            Client.builder().id(2L).name("Diego Maradona").email("diego.maradona@example.com").build()
        );
        when(clientService.getAllClients()).thenReturn(clients);

        List<Client> result = clientController.getAllClients();

        mockMvc.perform(get("/clients"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(clients.size())));
    }

    @Test
    void testSaveClient() throws Exception {
        Client clientToSave = new Client();
        clientToSave.setName("Carlos Tevez");
        clientToSave.setEmail("carlos.tevez@example.com");

        when(clientService.saveClient(any(Client.class))).thenReturn(clientToSave);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clientToSave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos Tevez"))
                .andExpect(jsonPath("$.email").value("carlos.tevez@example.com"));

        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    void testDeleteClient() throws Exception {
        Long clientId = 1L;

        mockMvc.perform(delete("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("ok"));

        verify(clientService, times(1)).deleteClient(clientId);
    }

    @Test
    void testPages() throws Exception {
        ClientFilterParams clientParams = new ClientFilterParams();
        clientParams.setName("Aymar");

        Page<Client> pagedClients = createMockPage();

        when(clientService.getAllClientsWithFilter(any(), any())).thenReturn(pagedClients);

        mockMvc.perform(post("/clients/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clientParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is((int) pagedClients.getTotalElements())));

        verify(clientService, times(1)).getAllClientsWithFilter(any(), any());
    }

    public static Page<Client> createMockPage() {
        List<Client> clients = Collections.singletonList(
                Client.builder().id(1L).name("Pablo Aymar").email("aymar@example.com").build()
        );
        return new PageImpl<>(clients, PageRequest.of(0, 1), clients.size());
    }

}
