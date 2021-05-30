package com.springboot.rest.model.dto.response;

import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel(value = "ApiMessageResponse", description = "Contains the success response message along with the id of the resource handled")
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ApiMessageResponse {

    private String message;

    private Long resourceId;

    public ApiMessageResponse(String message) {
        this.message = message;
    }

    public ApiMessageResponse(Long resourceId)
    {
        this.message = "Action successful";
        this.resourceId = resourceId;
    }


    public ApiMessageResponse(String message, Long resourceId)
    {
        this.message = message;
        this.resourceId = resourceId;
    }
}
