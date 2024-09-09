package com.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsNoOfferResponse {

    private Long id;

    private String title;

    private Long categoryID;

}
