package com.cookerytech.controller;


import com.cookerytech.domain.User;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.request.FavoriteUpdateRequest;
import com.cookerytech.dto.FavoriteDTO;
import com.cookerytech.dto.ProductDTO;
import com.cookerytech.service.FavoriteService;
import com.cookerytech.service.UserService;
import com.cookerytech.dto.response.CTResponse;
import com.cookerytech.dto.response.ResponseMessage;
import com.cookerytech.service.FavoriteService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final UserService userService;

    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @PostMapping("/auth")  // K02
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER') or hasRole('CUSTOMER')" +
            " or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelDTO> updateFavorite(@Valid @RequestBody FavoriteUpdateRequest modelId) {

        User currentUser = userService.getCurrentUser();
        ModelDTO modelDTO = favoriteService.toggleFavorite(/*currentUser,*/ modelId);

        return ResponseEntity.ok(modelDTO);
    }

    @GetMapping("/auth")//K01
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER') or " +
            " hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<FavoriteDTO>> getFavoritesByCurrentlyUser(){

        List<FavoriteDTO>  favoriteDTOS = favoriteService.getFavoritesByCurrentlyUser();

        return ResponseEntity.ok(favoriteDTOS);
    }

    @DeleteMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER') or " +
            " hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<CTResponse> deleteFavouritesFromAuthUsers(){ // K03

        favoriteService.deleteAllFavorites();
        CTResponse response = new CTResponse();
        response.setMessage(ResponseMessage.FAVORITE_DELETE_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);

    }

    @PutMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER') or " +
            " hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<CTResponse> moveUsersFavoritesToCart(){  //K04

     favoriteService.moveUsersFavoritesToCart();
     CTResponse response = new CTResponse();
     response.setMessage(ResponseMessage.USERS_FAVORITES_MOVED_TO_CART);
     response.setSuccess(true);

     return ResponseEntity.ok(response);

    }




}