package com.cookerytech.mapper;


import com.cookerytech.domain.Product;
import com.cookerytech.domain.ProductPropertyKey;
import com.cookerytech.dto.ProductPropertyKeyDTO;
import com.cookerytech.dto.request.ProductPropertyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductPropertyKeyMapper {


   @Mapping(source="product", target="productId", qualifiedByName="getProductId")
   ProductPropertyKeyDTO productPropertyKeyToProductPropertyKeyDTO(ProductPropertyKey productPropertyKey);

   @Mapping(source = "productId",target = "product", ignore = true)
   ProductPropertyKey productPropertyKeyRequestToProductPropertyKey(ProductPropertyRequest productPropertyRequest);

   List<ProductPropertyKeyDTO> map(List<ProductPropertyKey> productPropertyKeys);

   @Named("getProductId")
   public static Long getProductId(Product product) {
      return product.getId();
   }

}
