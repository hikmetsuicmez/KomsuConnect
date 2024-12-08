package com.hikmetsuicmez.komsu_connect.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean result;
    private String errorMessage;
    private T data;



    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> result = new ApiResponse<T>();
        result.setResult(true);
        result.setErrorMessage(null);
        result.setData(data);
        return result;
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        ApiResponse<T> result = new ApiResponse<T>();
        result.setResult(false);
        result.setErrorMessage(errorMessage);
        result.setData(null);
        return result;
    }

}
