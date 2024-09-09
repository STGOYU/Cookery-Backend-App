package com.cookerytech.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessage {

    @NotBlank(message = "Please provide your name")
    private String name;

    @NotBlank(message = "Please provide your company")
    private String company;

    @Email
    @NotBlank(message = "Please provide your email")
    private String email;

    @NotBlank(message = "Please provide your phone number")
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    private String phone;

    @NotBlank(message = "Please provide your message")
    private String message;
}
