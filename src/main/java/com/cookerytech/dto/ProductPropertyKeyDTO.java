package com.cookerytech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyKeyDTO {


    private Long id;
    private String name;
    private Integer seq;
    private Boolean builtIn;
    private Long productId;
}
