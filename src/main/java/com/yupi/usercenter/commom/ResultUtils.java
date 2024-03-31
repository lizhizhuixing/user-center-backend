package com.yupi.usercenter.commom;

public class ResultUtils {
    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"");
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode.getCode(),message,description);
    }
    public static <T> BaseResponse<T> error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),description);
    }
    public static <T> BaseResponse<T> error(int code,String message,String description){
        return new BaseResponse(code,message,description);
    }
}
