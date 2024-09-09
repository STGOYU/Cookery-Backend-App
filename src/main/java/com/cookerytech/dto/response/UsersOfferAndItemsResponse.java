package com.cookerytech.dto.response;

import com.cookerytech.domain.Currency;
import com.cookerytech.domain.Offer;
import com.cookerytech.domain.enums.OfferStatus;
import com.cookerytech.dto.OfferDTO;
import com.cookerytech.service.OfferItemService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersOfferAndItemsResponse {

    private Long id;
    private LocalDateTime createAt;
    private LocalDate deliveryAt;
    private String code;
    private OfferStatus status;
    private Double grandTotal;
    private Currency currency;
    private Double discount;
    private Long UserId;
    private int items=0;

    public UsersOfferAndItemsResponse(Offer offer,int items) {
        this.id = offer.getId();
        this.createAt = offer.getCreateAt();
        this.deliveryAt = offer.getDeliveryAt();
        this.code = offer.getCode();
        this.status = offer.getStatus();
        this.grandTotal = offer.getGrandTotal();
        this.currency = offer.getCurrency();
        this.discount = offer.getDiscount();
        UserId = offer.getUser().getId();
        this.items = items;
    }
}
