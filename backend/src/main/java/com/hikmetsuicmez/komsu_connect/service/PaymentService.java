package com.hikmetsuicmez.komsu_connect.service;

import com.iyzipay.model.Payment;

public interface PaymentService {

        Payment createPayment(String price, String cardHolderName, String cardNumber, String expireMonth, String expireYear, String cvc);
}
