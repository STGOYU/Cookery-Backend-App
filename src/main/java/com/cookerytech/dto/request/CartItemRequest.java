package com.cookerytech.dto.request;

import com.cookerytech.domain.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {

    private Long modelId;

    private Integer amount;
}
