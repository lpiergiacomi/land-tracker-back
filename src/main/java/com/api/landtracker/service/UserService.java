package com.api.landtracker.service;

import com.api.landtracker.model.dto.IUserWithAssignedLots;
import com.api.landtracker.model.dto.UserRegisterDTO;
import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRegisterDTO userRegisterationDto) throws DataIntegrityViolationException {
        try {
            User user = new User();
            user.setUsername(userRegisterationDto.getUsername());
            user.setPassword(passwordEncoder.encode(userRegisterationDto.getPassword()));
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("No se pudo registrar");
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserWithAssignedLotsDTO> getAllUsersWithAssignedLots() {
        List<IUserWithAssignedLots> usersWithAssignedLots = userRepository.getUserOrUsersWithAssignedLots(null);
        List<UserWithAssignedLotsDTO> usersWithAssignedLotsImpl = new ArrayList<>();

        usersWithAssignedLots.forEach(user -> {
            usersWithAssignedLotsImpl.add(getUserWithAssignedLotsDTO(user));
        });
        return usersWithAssignedLotsImpl;
    }

    private UserWithAssignedLotsDTO getUserWithAssignedLotsDTO(IUserWithAssignedLots user) {
        UserWithAssignedLotsDTO userToAdd = new UserWithAssignedLotsDTO();
        userToAdd.setId(user.getId());
        userToAdd.setUsername(user.getUsername());
        if (user.getAssignedLotsIdsString() != null) {
            List<Long> lotsIds = Arrays.stream(user.getAssignedLotsIdsString()
                                        .split(", ")).toList()
                                        .stream().map(Long::parseLong)
                                        .collect(Collectors.toList());
            userToAdd.setAssignedLotsIds(lotsIds);
        }
        return userToAdd;
    }

    public UserWithAssignedLotsDTO getUserById(Long id) {
        List<IUserWithAssignedLots> usersWithAssignedLots = userRepository.getUserOrUsersWithAssignedLots(id);
        return getUserWithAssignedLotsDTO(usersWithAssignedLots.get(0));
    }
}
