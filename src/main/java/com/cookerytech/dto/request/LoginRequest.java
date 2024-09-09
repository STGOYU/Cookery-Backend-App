package com.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    // This is my change//
    // This is my change//
    // This is my change//
    @Email(message = "Please provide a valid email")
    private String email;

    //This is my change//
    //This is my change//
    //This is my change//
    @NotBlank(message = "Please provide a password")
    private String password;
}
