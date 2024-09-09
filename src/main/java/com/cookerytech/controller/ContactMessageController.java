package com.cookerytech.controller;

import com.cookerytech.config.EmailConfig;
import com.cookerytech.dto.request.ContactMessage;
import com.cookerytech.exception.ValidationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact-message")
public class ContactMessageController {

    private final EmailConfig emailConfig;
    private final JavaMailSender mailSender;

    public ContactMessageController(EmailConfig emailConfig, JavaMailSender mailSender) {
        this.emailConfig = emailConfig;
        this.mailSender = mailSender;
    }
    @PostMapping
    public void sendContactMessage (@RequestBody ContactMessage contactMessage, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new ValidationException("Contact message is not valid");
        }


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailConfig.getConstantEmail());
        mailMessage.setTo(contactMessage.getEmail());
        mailMessage.setSubject(contactMessage.getName()+" - "+contactMessage.getCompany());
        mailMessage.setText("Phone:"+contactMessage.getPhone()+" - "+contactMessage.getMessage());

        mailSender.send(mailMessage);
    }
}
