package com.springboot.rest.model.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.rest.model.dto.user.UserProxyDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Post proxy", description = "Contains only the id and owner of the post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class  PostProxyDto {

    Long id;
    UserProxyDto owner;

    @JsonSerialize(as = Long.class)
    public Long getId() {
        return id;
    }

    @JsonDeserialize(as = Long.class)
    public void setId(Long id) {
        this.id = id;
    }
}
