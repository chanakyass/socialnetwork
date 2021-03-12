package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.dto.UserEditDto;
import com.springboot.rest.model.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class UserEditMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    //@Mapping(target = "grantedAuthoritiesList", ignore = true)
    public abstract void update(UserEditDto request, @MappingTarget User user);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    //@Mapping(target = "grantedAuthoritiesList", ignore = true)
    public abstract void toUserEdit(User request, @MappingTarget UserEditDto userEditDto);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    public abstract void toUserEdit(UserDto userDto, @MappingTarget UserEditDto userEditDto);

}
