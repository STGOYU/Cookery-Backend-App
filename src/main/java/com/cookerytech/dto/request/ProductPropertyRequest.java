package com.cookerytech.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyRequest {

    @NotBlank(message = "Please provide a property")
    private String name;

    private Integer seq;

    private Long productId;
}
