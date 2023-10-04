package com.api.landtracker.service;

import com.api.landtracker.model.dto.UserRegisterDTO;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRegisterDTO userRegisterationDto) {
        User user = new User();
        user.setUsername(userRegisterationDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterationDto.getPassword()));
        userRepository.saveAndFlush(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
