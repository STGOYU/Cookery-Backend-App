package com.cookerytech.dto.request;

import com.cookerytech.domain.Currency;
import com.cookerytech.domain.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferUpdate {

    private Double discount;

    private OfferStatus status;

    private Long currencyId;

}
