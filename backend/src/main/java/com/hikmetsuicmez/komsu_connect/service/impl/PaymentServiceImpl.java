package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import com.hikmetsuicmez.komsu_connect.service.PaymentService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

        @Value("${iyzico.api-key}")
        private String apiKey;

        @Value("${iyzico.secret-key}")
        private String secretKey;

        @Value("${iyzico.base-url}")
        private String baseUrl;

        private final UserService userService;
        private final CartService cartService;

        public Payment createPayment(String price, String cardHolderName, String cardNumber,
                                     String expireMonth, String expireYear, String cvc) {
                log.info("Creating payment with price: {}", price);

                Options options = new Options();
                options.setApiKey(apiKey);
                options.setSecretKey(secretKey);
                options.setBaseUrl(baseUrl);

                CreatePaymentRequest request = new CreatePaymentRequest();
                request.setLocale(Locale.TR.getValue());
                request.setConversationId(UUID.randomUUID().toString());
                request.setPrice(new BigDecimal(price));
                request.setPaidPrice(new BigDecimal(price));
                request.setCurrency(Currency.TRY.name());
                request.setInstallment(1);
                request.setBasketId(UUID.randomUUID().toString());
                request.setPaymentChannel(PaymentChannel.WEB.name());
                request.setPaymentGroup(PaymentGroup.LISTING.name()); // Changed from PRODUCT to LISTING

                PaymentCard paymentCard = new PaymentCard();
                paymentCard.setCardHolderName(cardHolderName);
                paymentCard.setCardNumber(cardNumber);
                paymentCard.setExpireMonth(expireMonth);
                paymentCard.setExpireYear(expireYear);
                paymentCard.setCvc(cvc);
                paymentCard.setRegisterCard(0);
                request.setPaymentCard(paymentCard);

                User currentUser = userService.getCurrentUser();

                Buyer buyer = new Buyer();
                buyer.setId(currentUser.getId().toString());
                buyer.setName(currentUser.getFirstName());
                buyer.setSurname(currentUser.getLastName());
                buyer.setGsmNumber(currentUser.getPhoneNumber());
                buyer.setEmail(currentUser.getEmail());
                buyer.setIdentityNumber("74300864791");
                buyer.setLastLoginDate("2015-10-05 12:43:35");
                buyer.setRegistrationDate("2013-04-21 15:12:09");
                buyer.setRegistrationAddress(currentUser.getNeighborhood());
                buyer.setIp("85.34.78.112");
                buyer.setCity("Istanbul");
                buyer.setCountry("Turkey");
                buyer.setZipCode("34732");
                request.setBuyer(buyer);

                Address shippingAddress = new Address();
                shippingAddress.setContactName(currentUser.getFirstName() + " " + currentUser.getLastName());
                shippingAddress.setCity("Istanbul");
                shippingAddress.setCountry("Turkey");
                shippingAddress.setAddress(currentUser.getNeighborhood());
                shippingAddress.setZipCode("34742");
                request.setShippingAddress(shippingAddress);

                Address billingAddress = new Address();
                billingAddress.setContactName(currentUser.getFirstName() + " " + currentUser.getLastName());
                billingAddress.setCity("Istanbul");
                billingAddress.setCountry("Turkey");
                billingAddress.setAddress(currentUser.getNeighborhood());
                billingAddress.setZipCode("34742");
                request.setBillingAddress(billingAddress);

                List<BasketItem> basketItems = new ArrayList<>();

                List<CartItemResponse> products = cartService.viewCart(currentUser.getId());

                BasketItem basketItem = new BasketItem();

                basketItem.setId("BI101");
                basketItem.setName("Test Product");
                basketItem.setCategory1("Test Category");
                basketItem.setItemType(BasketItemType.PHYSICAL.name());
                basketItem.setPrice(new BigDecimal(price));
                basketItems.add(basketItem);
                request.setBasketItems(basketItems);

                try {
                        log.info("Sending payment request to Iyzico");
                        Payment payment = Payment.create(request, options);
                        log.debug("Payment response: {}", payment.getErrorMessage());
                        return payment;
                } catch (Exception e) {
                        log.error("Payment failed", e);
                        throw new RuntimeException("Payment failed: " + e.getMessage());
                }
        }
}
