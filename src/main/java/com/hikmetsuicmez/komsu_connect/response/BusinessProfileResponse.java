package com.hikmetsuicmez.komsu_connect.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BusinessProfileResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String neighborhood;
    private String businessName;
    private String businessDescription;
    private List<ProductResponse> products;
}

