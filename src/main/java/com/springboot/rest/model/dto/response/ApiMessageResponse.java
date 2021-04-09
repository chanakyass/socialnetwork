package com.springboot.rest.model.dto.response;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ApiMessageResponse", description = "Contains the success response message along with the id of the resource handled")
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

    public ApiMessageResponse() {
        message = "Action Successful";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
