package com.hikmetsuicmez.komsu_connect.handler;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ErrorResponse<E>(
        LocalDateTime timestamp,
        Integer status,
        String error,
        Map<String, String> messages,
        String hostName,
        String path
) {

}
