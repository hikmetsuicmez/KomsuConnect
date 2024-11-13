package com.hikmetsuicmez.komsu_connect.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ServiceProfileResponse {

    private Long id;
    private String serviceName;
    private String description;
}
