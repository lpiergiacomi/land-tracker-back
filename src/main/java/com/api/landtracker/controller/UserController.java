package com.api.landtracker.controller;

import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/with-assigned-lots")
    public List<UserWithAssignedLotsDTO> getAllUsersWithAssignedLots() {
        return userService.getAllUsersWithAssignedLots();
    }
    @GetMapping("/{id}/with-assigned-lots")
    public UserWithAssignedLotsDTO getUserWithAssignedLots(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
