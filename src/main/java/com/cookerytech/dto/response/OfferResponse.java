package com.cookerytech.dto.response;

import com.cookerytech.domain.enums.OfferStatus;


import java.time.LocalDateTime;

public class OfferResponse {

    private String code;

    private OfferStatus status=OfferStatus.CREATED;

    private Double subTotal;

    private Double discount;

    private Double grandTotal;

    private Long userId;

    private String currencyCode;

    private LocalDateTime deliveryAt;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
