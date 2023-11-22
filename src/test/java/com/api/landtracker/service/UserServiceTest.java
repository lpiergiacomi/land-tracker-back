package com.api.landtracker.service;

import com.api.landtracker.model.dto.IUserWithAssignedLots;
import com.api.landtracker.model.dto.UserRegisterDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterUserSuccessfully() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("testUser");
        userRegisterDTO.setPassword("testPassword");

        when(userRepository.saveAndFlush(any())).thenReturn(new User());

        assertDoesNotThrow(() -> userService.register(userRegisterDTO));

        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void testRegisterUserWithDuplicateUsername() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("duplicateUser");
        userRegisterDTO.setPassword("testPassword");

        when(userRepository.saveAndFlush(any()))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userService.register(userRegisterDTO)
        );

        assertEquals("No se pudo registrar", exception.getMessage());

        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void testGetAllUsersWithAssignedLots() {
        IUserWithAssignedLots userWithAssignedLots = mock(IUserWithAssignedLots.class);
        when(userRepository.getUserOrUsersWithAssignedLots(null)).thenReturn(Collections.singletonList(userWithAssignedLots));

        List<UserWithAssignedLotsDTO> usersWithAssignedLots = userService.getAllUsersWithAssignedLots();

        assertThat(usersWithAssignedLots, hasSize(1));
        verify(userRepository, times(1)).getUserOrUsersWithAssignedLots(null);
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        IUserWithAssignedLots userWithAssignedLots = mock(IUserWithAssignedLots.class);
        when(userRepository.getUserOrUsersWithAssignedLots(userId)).thenReturn(Collections.singletonList(userWithAssignedLots));

        UserWithAssignedLotsDTO userDTO = userService.getUserById(userId);

        assertNotNull(userDTO);
        verify(userRepository, times(1)).getUserOrUsersWithAssignedLots(userId);
    }

}
