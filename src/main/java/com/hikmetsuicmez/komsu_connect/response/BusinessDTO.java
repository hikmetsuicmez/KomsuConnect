package com.hikmetsuicmez.komsu_connect.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDTO {
    private Long id;
    private String businessName;
    private String businessDescription;
    private Double rating;
    private String photoUrl;
    private String neighborhood;


}
