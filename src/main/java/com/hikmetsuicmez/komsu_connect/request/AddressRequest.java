package com.hikmetsuicmez.komsu_connect.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
        private String contactName;
        private String city;
        private String country;
        private String address;
        private String zipCode;

}