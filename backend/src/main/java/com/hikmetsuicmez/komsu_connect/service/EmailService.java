package com.hikmetsuicmez.komsu_connect.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}
