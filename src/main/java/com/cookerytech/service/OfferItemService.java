package com.cookerytech.service;

import com.cookerytech.domain.*;
import com.cookerytech.dto.OfferItemDTO;
import com.cookerytech.dto.request.OfferItemsUpdate;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.OfferItemMapper;
import com.cookerytech.repository.OfferItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferItemService {

    private final OfferItemRepository offerItemRepository;
    private final UserService userService;

    private final OfferItemMapper offerItemMapper;

    private final OfferService offerService;



    public OfferItemService(OfferItemRepository offerItemRepository, UserService userService, OfferItemMapper offerItemMapper, OfferService offerService) {
        this.offerItemRepository = offerItemRepository;
        this.userService = userService;
        this.offerItemMapper = offerItemMapper;
        this.offerService = offerService;
    }


    public List<OfferItem> getOfferItems(Long offerId) {
        List<OfferItem> offerItems = offerItemRepository.getByOfferItemByOfferId(offerId);
        return offerItems;
    }

    //Satışı yapılan ürün miktarını return ediyor.
    public List<Integer> stockAmountDecrease(Long offerId) {
        List<OfferItem> offerItems = getOfferItems(offerId);
        List<Integer>salesAmount = new ArrayList<>();

        for (int i=0; i<offerItems.size(); i++){
            int quantity = offerItems.get(i).getQuantity();
            int oldStockAmount = offerItems.get(i).getModel().getStockAmount();
            int newStockAmount = oldStockAmount-quantity;
            offerItems.get(i).getModel().setStockAmount(newStockAmount);
            salesAmount.add(quantity);
        }
        return salesAmount;
    }

    public Boolean existsOfferItemsByProductId(Long productId) {
       return offerItemRepository.existsByProductId(productId);

    }

    public OfferItemDTO updateOfferItems(Long id, OfferItemsUpdate offerItemsUpdate) {

        OfferItem offerItem = getOfferItem(id);
        Offer offer = new Offer();

        if((offer.getStatus().name().equals("CREATED") || offer.getStatus().name().equals("REJECTED"))){

            offerItem.setQuantity(offerItemsUpdate.getQuantity());
            offerItem.setSellingPrice(offerItemsUpdate.getPrice());
            offerItem.setTax(offerItemsUpdate.getTax());
            offerItem.setDiscount(offerItemsUpdate.getDiscount());

            offerItem.setSubTotal(offerItemsUpdate.getPrice() * offerItemsUpdate.getDiscount() * (1 + offerItemsUpdate.getTax() / 100));

        }else {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        OfferItem updateOfferItem = offerItemRepository.save(offerItem);

        OfferItemDTO offerItemDTO = offerItemMapper.OfferItemToOfferItemDTO(updateOfferItem);

        return offerItemDTO;

    }

    public OfferItemDTO deleteOfferItemsById(Long id) {

        OfferItem offerItem = getOfferItem(id);
        Offer offer = new Offer();

        if((offer.getStatus().name().equals("CREATED") || offer.getStatus().name().equals("REJECTED"))){

            offerItemRepository.delete(offerItem);

        }else {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        return offerItemMapper.OfferItemToOfferItemDTO(offerItem);


    }

    public OfferItem getOfferItem(Long id) {
        OfferItem offerItem = offerItemRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id))
        );
        return offerItem;
    }


    public OfferItem offerItemsCreate(Cart_Items cartItems, Offer offer) {


        Double profitRate =3.5; //productService.getBrandByProductId(productId);
        Integer quantity = cartItems.getAmount();
        Double sellPrice = calculateSellPrice(cartItems.getModel().getBuyingPrice(), profitRate);
        Double subTotal = calculateSubTotal(sellPrice, quantity, cartItems.getModel().getTaxRate());

        OfferItem offerItem = new OfferItem();
        offerItem.setUpdateAt(LocalDateTime.now());
        offerItem.setCreateAt(LocalDateTime.now());
        offerItem.setModel(cartItems.getModel());
        offerItem.setProduct(cartItems.getProduct());
        offerItem.setQuantity(quantity);
        offerItem.setSku(cartItems.getModel().getSku());
        offerItem.setOffer(offer);
        offerItem.setTax(cartItems.getModel().getTaxRate());
        offerItem.setSellingPrice(sellPrice);
        offerItem.setSubTotal(subTotal);

        return offerItemRepository.save(offerItem);
    }

    public Double calculateSubTotal(Double sellPrice, Integer quantity, Double tax){
        Double subTotal = sellPrice * quantity * (1 + (tax/100));
        return subTotal;
    }

    public Double calculateSellPrice(Double buyPrice, Double profitRate){
        Double sellPrice = buyPrice + (buyPrice*profitRate);
        return sellPrice;
    }


    public Boolean existsOfferItemsByModel(Model model) {
     return    offerItemRepository.existsByModel(model);
    }
}
