package org.example.chat.mapper;

import org.example.chat.dto.UserDto;
import org.example.chat.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);
    UserDto toDto(User user);
}
