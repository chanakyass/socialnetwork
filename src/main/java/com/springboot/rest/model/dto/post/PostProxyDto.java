package com.springboot.rest.model.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Post proxy", description = "Contains only the id and owner of the post")
public class  PostProxyDto {

    Long id;
    UserProxyDto owner;

    public PostProxyDto(Long id, UserProxyDto owner) {
        this.id = id;
        this.owner = owner;
    }

    public PostProxyDto() {
    }

    @JsonSerialize(as = Long.class)
    public Long getId() {
        return id;
    }

    @JsonDeserialize(as = Long.class)
    public void setId(Long id) {
        this.id = id;
    }

    public UserProxyDto getOwner() {
        return owner;
    }

    public void setOwner(UserProxyDto owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostProxyDto)) return false;
        PostProxyDto that = (PostProxyDto) o;
        return getId().equals(that.getId()) && getOwner().equals(that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwner());
    }
}
