package com.hikmetsuicmez.komsu_connect.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String neighborhood;

}