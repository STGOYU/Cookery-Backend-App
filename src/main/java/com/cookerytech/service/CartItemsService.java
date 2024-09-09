package com.cookerytech.service;

import com.cookerytech.domain.*;
import com.cookerytech.dto.response.CartResponse;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.ModelMapper;
import com.cookerytech.mapper.ProductMapper;
import com.cookerytech.repository.CartItemRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemsService {
    private final UserService userService;

    private final CartItemRepository cartItemRepository;

    private final ModelService modelService;

    private final CartService cartService;

    private final ProductMapper productMapper;

    private final ModelMapper modelMapper;

    public CartItemsService(CartItemRepository cartItemRepository, ModelService modelService, @Lazy UserService userService, CartService cartService, ProductMapper productMapper, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.modelService = modelService;
        this.userService = userService;
        this.cartService = cartService;
        this.productMapper = productMapper;
        this.modelMapper = modelMapper;
    }
    @Transactional
    public List<CartResponse> getCart() {
        User user = userService.getCurrentUser();

        Cart cart = cartItemRepository.getir(user.getId());
        List<Cart_Items> cartItemList = cartItemRepository.getirCartItems(cart.getId());

        if (cartItemList.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ErrorMessage.NO_DATA_IN_DB_TABLE_MESSAGE, "cartList"));
        }

        List<CartResponse> newCartResponse = new ArrayList<>();

        for (Cart_Items cartItems : cartItemList) {

            CartResponse cartResponse = new CartResponse(cartItems.getId(),
                    productMapper.productToProductDTO(cartItems.getProduct()),
                    modelMapper.modelToModelDTO(cartItems.getModel()),
                    cartItems.getAmount());

            newCartResponse.add(cartResponse);
        }
        return newCartResponse;
    }
    @Transactional
    public CartResponse manageCartItems(Long modelId, Integer amount) {

        User user = userService.getCurrentUser();
        Cart_Items cartItems = new Cart_Items();

        if (cartService.getCartByUser(user).isEmpty()){
            Cart cart = cartService.createCartByUserId(user);

            Model model = modelService.getModelById(modelId);
            cartItems.setCart(cart);
            cartItems.setAmount(amount);
            cartItems.setCreatedAt(LocalDateTime.now());
            cartItems.setModel(model);
            cartItems.setProduct(model.getProduct());
            cartItemRepository.save(cartItems);
        } else {
            if (cartItemRepository.getCartItemsByModelIdandByUser(modelId,user).isEmpty()){
                Cart cart = cartService.getCartByUser(user).orElseThrow( ()->
                        new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION));

                Model model = modelService.getModelById(modelId);
                cartItems.setCart(cart);
                cartItems.setAmount(amount);
                cartItems.setCreatedAt(LocalDateTime.now());
                cartItems.setModel(model);
                cartItems.setProduct(model.getProduct());
                cartItemRepository.save(cartItems);

            }else {
                Cart_Items cartItem = cartItemRepository.getCartItemsByModelIdandByUser(modelId,user).orElseThrow( ()->
                        new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION));
                if (amount == 0) {
                    cartItemRepository.delete(cartItem);
                    return new CartResponse();
                } else {
                    cartItem.setAmount(amount);
                    cartItem.setUpdatedAt(LocalDateTime.now());
                    Cart_Items updatedCartItem = cartItemRepository.save(cartItem);
                    return new CartResponse(
                            updatedCartItem.getId(),
                            productMapper.productToProductDTO(updatedCartItem.getProduct()),
                            modelMapper.modelToModelDTO(updatedCartItem.getModel()),
                            updatedCartItem.getAmount()
                    );
                }
            }
        }
        return new CartResponse(
                cartItems.getId(),
                productMapper.productToProductDTO(cartItems.getProduct()),
                modelMapper.modelToModelDTO(cartItems.getModel()),
                cartItems.getAmount()
        );

    }

    public List<Long> getCartItemsByProductId(Long productId) {
        List<Cart_Items>  cartItemsList = cartItemRepository.getCartItemsByProductId(productId);
        return cartItemsList.stream().map(cartItems -> cartItems.getId()).collect(Collectors.toList());
    }

    public void deleteCartItem(Long id) {
        getCartItemById(id);
        cartItemRepository.deleteById(id);
    }

    private Cart_Items getCartItemById(Long id){
        return    cartItemRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));
    }

    public List<Long> getCartItemIdsByModel(Model model) {
     return  cartItemRepository.findAllByModel(model).stream().map(cI->cI.getId()).collect(Collectors.toList());
    }

    public List<Cart_Items> getCartItemsForOfferItem(Long userId) {
        return cartItemRepository.getCartItemsByUserId(userId);
    }
}
