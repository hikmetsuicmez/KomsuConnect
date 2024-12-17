package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.request.PaymentRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.service.PaymentService;
import com.iyzipay.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

        private final PaymentService paymentService;

        @PostMapping
        public ApiResponse<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
                Payment payment = paymentService.createPayment(
                            paymentRequest.getPrice(),
                            paymentRequest.getCardHolderName(),
                            paymentRequest.getCardNumber(),
                            paymentRequest.getExpireMonth(),
                            paymentRequest.getExpireYear(),
                            paymentRequest.getCvc()
                );

                if (payment.getStatus().equals("success")) {
                        return ApiResponse.success(payment);
                } else {
                        return ApiResponse.error(payment.getErrorMessage());
                }
        }
}
