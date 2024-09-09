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
public class ResetPasswordRequest {

    @Email(message = "Please provide a valid email")
    private String code;

    @NotBlank(message="Please Provide New Password")
    private String newPassword;
}
