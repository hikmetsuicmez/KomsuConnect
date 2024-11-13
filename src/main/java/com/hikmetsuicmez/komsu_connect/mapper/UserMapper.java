package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.response.ServiceProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
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

    public UserProfileResponse toUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .neighborhood(user.getNeighborhood())
                .serviceProfile(user.getServiceProfile() != null
                        ? ServiceProfileResponse.builder()
                        .id(user.getServiceProfile().getId())
                        .serviceName(user.getServiceProfile().getServiceName())
                        .description(user.getServiceProfile().getDescription())
                        .build()
                        : null) // Null kontrolü yapıldı
                .build();
    }

}
