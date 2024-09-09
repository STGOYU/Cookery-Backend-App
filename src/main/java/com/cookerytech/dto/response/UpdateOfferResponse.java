package com.cookerytech.dto.response;


import com.cookerytech.dto.OfferDTO;
import com.cookerytech.dto.OfferItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOfferResponse {

    private OfferDTO offerDTO;

    private List<OfferItemDTO> offerItemDTOList;

}
