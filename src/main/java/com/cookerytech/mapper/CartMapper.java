package com.cookerytech.mapper;

import com.cookerytech.domain.Cart;
import com.cookerytech.dto.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface CartMapper {

//    @Mapping(source="user", target="userId", ignore = true)
//    List<CartResponse> cartToCartResponses(List<Cart> allCarts);
}
