package com.api.landtracker.controller;

import com.api.landtracker.model.entities.Client;
import com.api.landtracker.model.filter.ClientFilterParams;
import com.api.landtracker.service.ClientService;
import com.api.landtracker.utils.ApiResponse;
import com.api.landtracker.utils.exception.DataValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        // Arrange
        List<Client> clients = Arrays.asList(
            Client.builder().id(1L).name("Lionel Messi").email("lionel.messi@example.com").build(),
            Client.builder().id(2L).name("Diego Maradona").email("diego.maradona@example.com").build()
        );
        when(clientService.getAllClients()).thenReturn(clients);

        // Act
        List<Client> result = clientController.getAllClients();

        //when
        mockMvc.perform(get("/clients"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.length()", is(clients.size())));
    }

}
