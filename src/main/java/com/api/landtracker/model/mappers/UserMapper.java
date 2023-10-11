package com.api.landtracker.model.mappers;

import com.api.landtracker.model.dto.UserDTO;
import com.api.landtracker.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "user.id")
    UserDTO userToUserDTO(User user);

    List<UserDTO> usersToUserDTOs(List<User> users);
    List<User> usersDTOToUser(List<UserDTO> users);

}
