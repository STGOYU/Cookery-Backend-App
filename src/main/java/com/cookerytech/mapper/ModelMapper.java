package com.cookerytech.mapper;

import com.cookerytech.domain.*;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.response.ModelResponse;
import com.cookerytech.dto.response.OfferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    ModelResponse modelToModelResponse(Model model);

    @Mapping(source="images", target="imageIds", qualifiedByName = "getImageId")
    @Mapping(source="currency", target="currencyId", qualifiedByName = "getCurrencyId")
    @Mapping(source="product", target="productId", qualifiedByName = "getProductId")
    ModelDTO modelToModelDTO(Model model);

    List<ModelDTO> map(List<Model> modelList);
    @Named("getImageId")
    public static Set<String> getImageId(Set<Image> imageSet) {
        return imageSet.stream().map(image -> image.getId()).collect(Collectors.toSet());
    } @Named("getCurrencyId")
    public static Long getCurrencyId(Currency currency) {
        return currency.getId();
    } @Named("getProductId")
    public static Long getProductId(Product product) {
        return product.getId();
    }


}
