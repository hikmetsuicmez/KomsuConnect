package com.hikmetsuicmez.komsu_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {

    ROLE_USER("ROLE_USER"),
    ROLE_BUSINESS_OWNER("ROLE_BUSINESS_OWNER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.role.equals(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }

    @JsonValue
    public String getRole() {
        return role;
    }
}
