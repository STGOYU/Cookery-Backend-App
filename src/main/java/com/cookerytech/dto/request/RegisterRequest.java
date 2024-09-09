package com.cookerytech.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(min=2, max=30)
    @NotBlank(message="Please provide your First Name")
    private String firstName;

    @Size(min=2, max=30)
    @NotBlank(message="Please provide your Last Name")
    private String lastName;

    @Size(min=10 ,max=80)
    @Email(message = "Please provide valid e-mail")
    private String email;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    @Size(min=14, max=14)
    @NotBlank(message = "Please provide your phone number")
    private String phone;

    @Size(max=150)
    @NotBlank(message="Please provide your address")
    private String address;

    @Size(max=100)
    @NotBlank(message="Please provide your city")
    private String city;

    @Size(max=100)
    @NotBlank(message="Please provide your country")
    private String country;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Please provide your birth date")
    private LocalDate birthDate;

    @Size(max=150)
    @NotBlank(message="Please provide your tax number")
    private String taxNo;


    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", //Password1!
            message = "The password must contain at least one letter, one number and one special character")
    @Size(min=8 , max=120, message="Password must be at least 8 characters")
    @NotBlank(message="Please provide your password")
    private String password;









}
