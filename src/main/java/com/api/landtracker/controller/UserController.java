package com.api.landtracker.controller;

import com.api.landtracker.model.dto.UserWithAssignedLotsDTO;
import com.api.landtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

}
