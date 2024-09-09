package com.cookerytech.mapper;

import com.cookerytech.domain.*;
import com.cookerytech.dto.OfferItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferItemMapper {

    @Mapping(source="offer", target="offerId", qualifiedByName = "getofferId")
    @Mapping(source="model", target="modelId", qualifiedByName = "getmodelId")
    @Mapping(source="product", target="productId", qualifiedByName = "getproductId")
    //@Mapping(target="productName", qualifiedByName = "getproductName")
    OfferItemDTO OfferItemToOfferItemDTO(OfferItem offerItem);

    @Named("getofferId")
    public static Long getofferId(Offer offer) {
        return offer.getId();
    }

    @Named("getmodelId")
    public static Long getmodelId(Model model) {
        return model.getId();
    }

    @Named("getproductId")
    public static Long getProductrId(Product product) {
        return product.getId();
    }

//    @Named("getproductName")
//    public static String getProductName(Product product) {
//        return product.getTitle();
//    }


    List<OfferItemDTO> map(List<OfferItem> offerItems);


}
