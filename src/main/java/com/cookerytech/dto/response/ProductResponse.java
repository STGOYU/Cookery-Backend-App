package com.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Boolean isFeatured=false;
    private Boolean isActive=true;
    private Long categoryId;


}
