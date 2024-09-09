package com.cookerytech.dto;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.ModelPropertyValue;
import com.cookerytech.domain.ProductPropertyKey;
import com.cookerytech.dto.response.ProductPropertyKeyResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class ModelPropertyValueDTO {

    private Long id;

    private String value;

    private ProductPropertyKeyResponse productPropertyKeyResponse;

    public ModelPropertyValueDTO(ModelPropertyValue modelPropertyValue) {
        this.id = modelPropertyValue.getId();
        this.value = modelPropertyValue.getValue();
        this.productPropertyKeyResponse = new ProductPropertyKeyResponse(modelPropertyValue.getProductPropertyKey());
    }
}
