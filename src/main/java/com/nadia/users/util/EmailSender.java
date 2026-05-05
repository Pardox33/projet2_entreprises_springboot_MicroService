package com.nadia.users.util;

public interface EmailSender {
    void sendEmail(String toEmail, String body);
}