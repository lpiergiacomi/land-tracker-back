package com.api.landtracker.controller;

import com.api.landtracker.model.dto.UserRegisterDTO;
import com.api.landtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody UserRegisterDTO userRegisterDTO) throws DataIntegrityViolationException {
        userService.register(userRegisterDTO);
        return ResponseEntity.ok().build();
    }

}
