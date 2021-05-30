package com.springboot.rest.unit.data;

import com.springboot.rest.model.dto.user.UserDto;
import com.springboot.rest.model.dto.user.UserEditDto;
import com.springboot.rest.model.dto.user.UserProxyDto;
import com.springboot.rest.model.entities.Role;
import com.springboot.rest.model.entities.User;

import java.time.LocalDate;
import java.util.List;

public class UserTestDataFactory {

    public UserTestDataFactory(){
    }

    public User getRandomUser(){
        return new User(1L, "chan", "chan", "chan@rest.com", "kavi2210@",
                LocalDate.of(1995, 6, 10), "Love react",
                List.of(Role.builder().id(1L).authority("ROLE_USER").build()));
    }

    public User getRandomOtherUser(){
        User otherUser = getRandomUser();
        otherUser.setId(2L);
        otherUser.setName("Swathy");
        otherUser.setEmail("swathy@rest.com");
        return otherUser;
    }

    public UserProxyDto getRandomOtherUserProxyDto(){
        UserProxyDto otherUserProxyDto = getRandomUserProxyDto();
        otherUserProxyDto.setId(2L);
        otherUserProxyDto.setName("Swathy");
        otherUserProxyDto.setEmail("swathy@rest.com");
        return otherUserProxyDto;
    }

    public UserDto getRandomUserDto(){
        return new UserDto(1L, "chan", "chan", "chan@rest.com", "kavi2210@",
                LocalDate.of(1995, 6, 10), "Love react",
                List.of(Role.builder().id(1L).authority("ROLE_USER").build()));
    }

    public UserEditDto getRandomEditedUserDto(){
        UserDto userDto = getRandomUserDto();
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setId(userDto.getId());
        userEditDto.setName(userDto.getName());
        userEditDto.setEmail(userDto.getEmail());
        userEditDto.setUserSummary("Love fullstack");
        return userEditDto;
    }

    public UserProxyDto getRandomUserProxyDto(){
        return getRandomUserDto();
    }

    public UserProxyDto getRandomUserProxyDto(Long i){
        UserProxyDto otherUserProxyDto = getRandomUserProxyDto();
        otherUserProxyDto.setId(i);
        otherUserProxyDto.setName("Swathy"+i);
        otherUserProxyDto.setEmail("swathy"+i+"@rest.com");
        return otherUserProxyDto;
    }

    public User getRandomUser(Long i){
        User user = getRandomUser();
        user.setId(i);
        user.setName("Swathy"+i);
        user.setEmail("swathy"+i+"@rest.com");
        return user;
    }
}
