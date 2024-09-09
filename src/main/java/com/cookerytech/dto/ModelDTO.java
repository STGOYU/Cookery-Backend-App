package com.cookerytech.dto;

import com.cookerytech.domain.Currency;
import com.cookerytech.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelDTO {


    private Long id;


    private String title;


    private String sku;


    private Integer stockAmount;


    private Integer inBoxQuantity;


    private Integer seq;


   private Set<String> imageIds;   //fk


    private Double buyingPrice;


    private Double taxRate;


    private Boolean isActive;


    private Boolean builtIn;


    private Long currencyId;  //fk


    private Long productId;  //fk


    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
