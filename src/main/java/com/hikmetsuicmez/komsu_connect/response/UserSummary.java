package com.hikmetsuicmez.komsu_connect.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserSummary {
    private Long id;
    private String firstName;
    private String lastName;

}
