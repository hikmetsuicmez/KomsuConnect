package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
        private String price;

        @NotBlank(message = "Card holder name is required")
        private String cardHolderName;

        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "^[0-9]{16}$", message = "Invalid card number")
        private String cardNumber;

        @NotBlank(message = "Expire month is required")
        @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Invalid expire month")
        private String expireMonth;

        @NotBlank(message = "Expire year is required")
        @Pattern(regexp = "^20[2-9][0-9]$", message = "Invalid expire year")
        private String expireYear;

        @NotBlank(message = "CVC is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVC")
        private String cvc;

}