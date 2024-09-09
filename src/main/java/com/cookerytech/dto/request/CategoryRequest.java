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
public class CategoryRequest {

    @Size(min = 2, max = 80, message = "Your name '${validateValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide your title")
    private String title;

    @NotNull(message = "Please provide active")
    private Boolean isActive=true;

}
