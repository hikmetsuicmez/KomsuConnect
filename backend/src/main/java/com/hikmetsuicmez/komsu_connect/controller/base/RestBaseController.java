package com.hikmetsuicmez.komsu_connect.controller.base;

import com.hikmetsuicmez.komsu_connect.response.ApiResponse;

public class RestBaseController {

    public <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    public <T> ApiResponse<T> error(String errorMessage) {
        return ApiResponse.error(errorMessage);
    }
}
