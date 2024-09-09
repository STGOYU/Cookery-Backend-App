package com.cookerytech.dto.request;

import com.cookerytech.domain.Image;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ModelUpdateRequest {

    @NotBlank
    @Size(min = 5, max = 50)
    private String title;
    @NotBlank
    private String sku;
    @NotNull
    private Integer stockAmount;
    @NotNull
    private Integer inBoxQuantity;
    @NotNull
    private Integer seq;

    private MultipartFile image;
    @NotNull
    private Double buyingPrice;
    @NotNull
    private Double taxRate;
    @NotNull
    private Boolean isActive;
    @NotNull
    private Long currencyId;
    @NotNull
    private  Long productId;

    //private Boolean builtIn; // ?? Update edilebilecek mi?

}
