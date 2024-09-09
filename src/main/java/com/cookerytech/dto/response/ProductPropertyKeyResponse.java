package com.cookerytech.dto.response;

import com.cookerytech.domain.ProductPropertyKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyKeyResponse {
    private Long id;
    private String name;
    private Integer seq;

    public ProductPropertyKeyResponse(ProductPropertyKey productPropertyKey) {
        this.id = productPropertyKey.getId();
        this.name = productPropertyKey.getName();
        this.seq = productPropertyKey.getSeq();
    }
}
