package com.ivanjunckes.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ApiResult<E> {

    @JsonProperty("status_code")
    private Integer statusCode;

    private String status;

    private List<E> data = new ArrayList<>();

    private String message;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        boolean isOkOrCreatedOrNoContent = statusCode != null && (statusCode == 200 || statusCode == 201 || statusCode == 204);
        if(isOkOrCreatedOrNoContent){
            return "success";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
