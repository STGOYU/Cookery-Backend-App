package com.cookerytech.dto.request;

import lombok.*;
import org.mapstruct.Mapping;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {



    @Size(min = 4,max=70)
    @NotBlank(message="Please provide the name of brand")
    private String name;

    @NotNull(message = "Please enter the profit rate as a decimalised value")
    private Double profitRate;


    private Boolean isActive;


    private Boolean builtIn;



}
