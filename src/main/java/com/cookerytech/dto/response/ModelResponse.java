package com.cookerytech.dto.response;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class ModelResponse {

    private String title;
    private String sku;
    private Integer stockAmount;
    private Integer inBoxQuantity;
    private Integer seq;
    private Integer imageId;
    private Double buyingPrice;
    private Double taxRate;
    private Boolean isActive;
    private Boolean builtIn;
    private Integer currencyId;
    private  Integer productId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
