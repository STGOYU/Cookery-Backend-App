package com.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandSaveRequest {

    @Size(min = 4, max = 70, message = "Your name '${validateValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide your name")
    private String name;

    @NotNull(message = "Please provide profit rate")
    private Double profitRate=0.0;

    @NotNull(message = "Please provide active")
    private Boolean isActive=true;

}
