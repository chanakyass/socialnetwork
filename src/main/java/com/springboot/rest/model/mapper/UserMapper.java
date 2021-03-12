package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract void toUser(UserDto request, @MappingTarget User user);

    public abstract void toUserDto(User request, @MappingTarget UserDto userDto);
}
