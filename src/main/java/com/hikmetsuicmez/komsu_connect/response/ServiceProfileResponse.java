package com.hikmetsuicmez.komsu_connect.response;

import lombok.Builder;

@Builder
public record ServiceProfileResponse(
        Long id,
        String serviceName,
        String description
) {
}
