package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.UserProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;

public class UserMapper {

    public static UserSummary toUserSummary(User user) {
        return UserSummary.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public static RegisterRequest toRegisterRequest(User user) {

        return RegisterRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .neighborhood(user.getNeighborhood())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static UserProfileResponse toUserProfileResponse(User user) {

        UserProfileResponse build = UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .neighborhood(user.getNeighborhood())
                .phoneNumber(user.getPhoneNumber())
                .build();

        ServiceProfileRequest profileRequest = ServiceProfileRequest.builder()
                .description(build.getServiceProfile().getDescription())
                .serviceName(build.getServiceProfile().getServiceName())
                .build();

        build.setServiceProfile(profileRequest);
        return build;
    }
}
