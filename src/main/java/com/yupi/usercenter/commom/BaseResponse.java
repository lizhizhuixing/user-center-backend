package com.yupi.usercenter.commom;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private String message;
    private T data;
    private int code;
    private String description;

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.message = message;
        this.data = data;
        this.code = code;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}

