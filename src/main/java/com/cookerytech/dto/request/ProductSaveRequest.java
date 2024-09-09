package com.cookerytech.dto.request;

import com.cookerytech.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveRequest {

    @Size(min = 5, max = 150, message = "Your name '${validateValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide your title")
    private String title;

    @Size(max = 300, message = "Your name '${validateValue}' must be {max} chars long")
    private String shortDesc;

    private String longDesc;

//    @Size(min = 5, max = 200, message = "Your name '${validateValue}' must be between {min} and {max} chars long")
//    @NotBlank(message = "Please provide your Sluq")
//    private String slug;

    @NotNull(message = "Please provide Seq")
    private Integer seq=0;

    @NotNull(message = "Please provide New Product")
    private Boolean isNew=false;

    @NotNull(message = "Please provide Featured")
    private Boolean isFeatured=false;

    @NotNull(message = "Please provide Active")
    private Boolean isActive=true;

    private Long brandsId;

    private Long categoryId;

    private Long brandId;

}
