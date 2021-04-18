package com.springboot.rest.model.mapper;

import com.springboot.rest.model.dto.likes.LikePostDto;
import com.springboot.rest.model.entities.LikePost;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LikePostMapper {

    public abstract LikePost toLikePost(LikePostDto likePostDto);

    public abstract LikePostDto toLikePostDto(LikePost likePost);

    public abstract List<LikePostDto> toLikePostDtoList(List<LikePost> likes);




}
