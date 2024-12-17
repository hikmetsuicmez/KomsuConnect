package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.request.AddressRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

        @PostMapping
        public ApiResponse<?> saveAddress(@RequestBody AddressRequest addressRequest) {
                // Burada adres bilgilerini veritabanına kaydedebilirsiniz
                // Şimdilik sadece başarılı bir yanıt döndürelim
                return ApiResponse.success("Address saved successfully");
        }
}
