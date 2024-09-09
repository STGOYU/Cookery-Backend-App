package com.cookerytech.mapper;

import com.cookerytech.domain.Brand;
import com.cookerytech.domain.Category;
import com.cookerytech.domain.Product;
import com.cookerytech.dto.ProductDTO;
import com.cookerytech.dto.request.ProductSaveRequest;
import com.cookerytech.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {

    @Mapping(source="brand", target="brandId", qualifiedByName = "getBrandIds")
    @Mapping(source="category", target="categoryId", qualifiedByName = "getCategoryId")
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> map(List<Product> products);
    @Named("getCategoryId")
    public static Long getCategoryId(Category category) {
        return category.getId();
    }

    @Named("getBrandIds")
    public static Long getBrandIds(Brand brand) {
       return brand.getId();
    }
    List<ProductResponse> productToProductResponse(List<Product> productList);


    //Product productSaveRequestToProduct(ProductSaveRequest productSaveRequest);

    //ProductDTO productToProductDTO(Product updateProduct);

}

