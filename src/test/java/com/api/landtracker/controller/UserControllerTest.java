package com.api.landtracker.controller;

import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsersWithAssignedLots() throws Exception {
        UserWithAssignedLotsDTO userDTO = new UserWithAssignedLotsDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");
        userDTO.setAssignedLotsIds(List.of(1L, 2L, 3L));

        when(userService.getAllUsersWithAssignedLots()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/users/with-assigned-lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(userDTO.getUsername())))
                .andExpect(jsonPath("$[0].assignedLotsIds", containsInAnyOrder(1, 2, 3)));

        verify(userService, times(1)).getAllUsersWithAssignedLots();
    }

    @Test
    void testGetUserWithAssignedLots() throws Exception {
        Long userId = 1L;
        UserWithAssignedLotsDTO userDTO = new UserWithAssignedLotsDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testUser");
        userDTO.setAssignedLotsIds(List.of(1L, 2L, 3L));

        when(userService.getUserById(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/users/{id}/with-assigned-lots", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDTO.getId().intValue())))
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())))
                .andExpect(jsonPath("$.assignedLotsIds", containsInAnyOrder(1, 2, 3)));

        verify(userService, times(1)).getUserById(userId);
    }

}
