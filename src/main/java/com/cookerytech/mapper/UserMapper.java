package com.cookerytech.mapper;

import com.cookerytech.domain.User;
import com.cookerytech.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserDTO userToUserDTO(User user);

}
