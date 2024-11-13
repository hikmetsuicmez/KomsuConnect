package com.hikmetsuicmez.komsu_connect.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {

    private String name;
    private String description;
    private Double price;
}
