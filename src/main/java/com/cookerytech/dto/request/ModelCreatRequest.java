package com.cookerytech.dto.request;

import com.cookerytech.domain.Currency;
import com.cookerytech.domain.Image;
import com.cookerytech.domain.ImageData;
import com.cookerytech.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelCreatRequest {

    @Size(min = 5,max=150)
    @NotBlank(message="Please provide the title of model")
    private String title;

    @NotBlank(message="Please provide the sku of model")
    private String sku;

    @NotNull(message="Please provide the stock amount of model")
    private Integer stockAmount;

    @NotNull(message="Please provide the inbox quantity of model")
    private Integer inBoxQuantity=1;

    @NotNull(message="Please provide the seq of model")
    private Integer seq=0;

    @NotNull(message="Please provide the image of model")
    private Set<MultipartFile> images;   //fk

    @NotNull(message="Please provide the buying price of model")
    private Double buyingPrice;

    @NotNull(message="Please provide the tax rate of model")
    private Double taxRate=0.0;

    @NotNull(message="Please provide the is active of model")
    private Boolean isActive=true;

    @NotNull(message="Please provide the currency Id of model")
    private Long currencyId;  //fk

    @NotNull(message="Please provide the product Id of model")
    private Long productId;  //fk

}
