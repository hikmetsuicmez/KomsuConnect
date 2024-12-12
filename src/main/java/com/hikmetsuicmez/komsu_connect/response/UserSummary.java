package com.hikmetsuicmez.komsu_connect.response;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSummary {

    private Long id;
    private String firstName;
    private String lastName;
    private String role;

    public UserSummary(Long userId, String senderName) {
    }
}
