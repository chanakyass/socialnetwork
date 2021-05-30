package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.dto.user.UserEditDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract User toUser(UserDto request);

    public abstract UserDto toUserDto(User request);

    public abstract UserProxyDto toUserProxyDto(UserDto request);

    public abstract UserEditDto toUserEditDto(UserDto userDto);
}
