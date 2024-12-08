package com.hikmetsuicmez.komsu_connect.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
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

