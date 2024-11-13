package com.hikmetsuicmez.komsu_connect.response;

import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String neighborhood;
    private ServiceProfileRequest serviceProfile;

}
