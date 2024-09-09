package com.cookerytech.controller;

import com.cookerytech.dto.request.CartItemRequest;
import com.cookerytech.dto.response.CartResponse;
import com.cookerytech.service.CartItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartItemsService cartItemsService;

    public CartController(CartItemsService cartItemService) {
        this.cartItemsService = cartItemService;
    }

    @GetMapping("/auth")
    public ResponseEntity<List<CartResponse>> getUsersCart(){

        List<CartResponse> cart = cartItemsService.getCart();

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/auth")
    public ResponseEntity<CartResponse> manageCartItem(@RequestBody CartItemRequest cartItemRequest) {

        CartResponse cartResponse = cartItemsService.manageCartItems(
                cartItemRequest.getModelId(),
                cartItemRequest.getAmount()
        );

        return ResponseEntity.ok(cartResponse);
    }

}
