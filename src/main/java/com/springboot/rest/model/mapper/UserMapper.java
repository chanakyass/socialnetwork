package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.UserDto;
import com.springboot.rest.model.entities.User;
import org.mapstruct.*;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "grantedAuthoritiesList", ignore = true)
    public abstract void update(UserDto request, @MappingTarget User user);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "grantedAuthoritiesList", ignore = true)
    public abstract void toUserEdit(User request, @MappingTarget UserDto userDto);

    @AfterMapping
    protected void afterUpdate(UserDto request, @MappingTarget User user) {
        if (request.getGrantedAuthoritiesList() != null) {
            user.setGrantedAuthoritiesList(request.getGrantedAuthoritiesList());
        }
    }



}
