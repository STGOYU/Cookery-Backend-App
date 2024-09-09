package com.cookerytech.service;


import com.cookerytech.domain.Favorite;
import com.cookerytech.domain.Model;
import com.cookerytech.domain.User;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.request.FavoriteUpdateRequest;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.ModelMapper;
import com.cookerytech.dto.FavoriteDTO;
import com.cookerytech.mapper.ProductMapper;
import com.cookerytech.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ModelService modelService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final CartItemsService cartItemsService;

    public FavoriteService(FavoriteRepository favoriteRepository,CartItemsService cartItemsService, ModelService modelService, ModelMapper modelMapper, UserService userService, ProductMapper productMapper, CartService cartService) {
        this.favoriteRepository = favoriteRepository;
        this.modelService = modelService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.productMapper = productMapper;
        this.cartService = cartService;
        this.cartItemsService = cartItemsService;
    }


    @Transactional
    public ModelDTO toggleFavorite(/*User currentUser,*/ FavoriteUpdateRequest modelId) {

//        User user = currentUser; // Get the authenticated user
        Model model = modelService.getModelById(modelId.getModelId());
        User authenticatedUser = userService.getCurrentUser();

        // Check if the model is already in favorites
        Favorite favorite = favoriteRepository.findByModelAndUser(model, authenticatedUser);
        if (favorite != null) {
            // Remove from favorites - Model is already a favorite, remove it
            favoriteRepository.delete(favorite);
        } else {
            // Add to favorites - Model is not a favorite, add it
            Favorite newFavorite = new Favorite();
            newFavorite.setModel(model);
            newFavorite.setUser(authenticatedUser);
            newFavorite.setCreateAt(LocalDateTime.now());
            favoriteRepository.save(newFavorite);
        }

        return modelMapper.modelToModelDTO(model);
    }

    @Transactional
    public List<FavoriteDTO> getFavoritesByCurrentlyUser() {

        User currentlyUser = userService.getCurrentUser();
        Long userId = currentlyUser.getId();

        List<Favorite>  favorites =  favoriteRepository.findAllByUserId(userId);

        List<FavoriteDTO> favoriteDTOS = favorites.stream().
                map(
                        favorite -> (
                                new FavoriteDTO(favorite,
                                modelMapper.modelToModelDTO(favorite.getModel()))
                        )
                ).
                collect(Collectors.toList());

        return favoriteDTOS;
    }

    public void deleteAllFavorites() {

        favoriteRepository.deleteAll();

    }

    @Transactional
    public void moveUsersFavoritesToCart() {  //K04
        //currently users favorites
      List<FavoriteDTO> usersFavorites = getFavoritesByCurrentlyUser();

        for (FavoriteDTO userFavorite : usersFavorites) {


          cartItemsService.manageCartItems(userFavorite.getModelDTO().getId(),1);




        }

    }


    public List<Long> getFavoritesByModelsOfProduct(Long productId) {
       List<Favorite> favorites =  favoriteRepository.getFavoritesByModelsOfProduct(productId);
       return  favorites.stream().map(favorite->favorite.getId()).collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId) {
      Favorite favorite =  getFavorite(favoriteId);
        favoriteRepository.delete(favorite);
    }

    public Favorite getFavorite(Long id){
    return     favoriteRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id))
        );

    }


    public List<Long> getFavoriteIdsByModel(Model model) {
     return favoriteRepository.findAllByModel(model).stream().map(f->f.getId()).collect(Collectors.toList());
    }
    public boolean isFavoriteByModelAndCurrentlyUser(Model model,User user){
      return   favoriteRepository.existsByModelAndUser(model,user);
    }
}