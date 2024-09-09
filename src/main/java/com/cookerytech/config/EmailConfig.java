package com.cookerytech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String constantEmail;

    public String getConstantEmail() {
        return constantEmail;
    }


    // test satırı

}
