package com.yashny.homehub_backend.mappers;

import com.yashny.homehub_backend.dto.SignUpDto;
import com.yashny.homehub_backend.dto.UserDto;
import com.yashny.homehub_backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
