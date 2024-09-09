package com.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {

    @Size(min = 2, max = 80)
    @NotBlank(message = "Please provide your title")
    private String title;

    private Boolean isActive=true;

    private Boolean builtIn;
}
