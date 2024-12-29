package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.request.PaymentRequest;
import com.hikmetsuicmez.komsu_connect.service.PaymentService;
import com.iyzipay.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

        @InjectMocks
        private PaymentController paymentController;

        @Mock
        private PaymentService paymentService;

        private MockMvc mockMvc;
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                objectMapper = new ObjectMapper();
                mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        }

        @Test
        void shouldCreatePaymentSuccessfullyWhenRequestIsValid() throws Exception {
                Payment payment = new Payment();
                payment.setStatus("success");
                payment.setErrorMessage(null);

                when(paymentService.createPayment(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(payment);

                PaymentRequest paymentRequest = PaymentRequest.builder()
                            .price("100")
                            .cardHolderName("Hikmet Suicmez")
                            .cardNumber("1234567890123456")
                            .expireMonth("12")
                            .expireYear("2023")
                            .cvc("123")
                            .build();

                mockMvc.perform(post("/payment")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(paymentRequest)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.errorMessage").doesNotExist());

                verify(paymentService, times(1)).createPayment(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        }

        @Test
        void shouldReturnErrorWhenPaymentFails() throws Exception {
                Payment payment = new Payment();
                payment.setStatus("failure");
                payment.setErrorMessage("Payment failed");

                when(paymentService.createPayment(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(payment);

                PaymentRequest paymentRequest = PaymentRequest.builder()
                            .price("100")
                            .cardHolderName("Hikmet Suicmez")
                            .cardNumber("1234567890123456")
                            .expireMonth("12")
                            .expireYear("2023")
                            .cvc("123")
                            .build();

                mockMvc.perform(post("/payment")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(paymentRequest)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.errorMessage").value("Payment failed"));

                verify(paymentService, times(1)).createPayment(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        }
}