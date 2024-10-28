package com.hikmetsuicmez.komsu_connect.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String neighborhood;
    private String profession;
}
