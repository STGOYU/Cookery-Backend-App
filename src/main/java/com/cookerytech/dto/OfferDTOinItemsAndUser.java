package com.cookerytech.dto;

import com.cookerytech.domain.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTOinItemsAndUser {

    private Long id;

    private String code;

    private OfferStatus status;

    List<OfferItemDTO> offerItemsDTO ;

    UserDTO userDTO;
}
