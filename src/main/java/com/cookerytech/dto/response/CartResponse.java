package com.cookerytech.dto.response;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.Product;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Long id;

    private ProductDTO product;

    private ModelDTO model;

    private Integer amount;

    public CartResponse(Long id, ProductDTO product, ModelDTO model, Integer amount) {
        this.id = id;
        this.product = product;
        this.model = model;
        this.amount = amount;
    }
}
