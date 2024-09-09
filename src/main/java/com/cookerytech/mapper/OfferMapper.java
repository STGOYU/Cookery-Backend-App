package com.cookerytech.mapper;

import com.cookerytech.domain.Offer;
import com.cookerytech.domain.OfferItem;
import com.cookerytech.dto.OfferDTO;
import com.cookerytech.dto.OfferItemDTO;
import com.cookerytech.dto.response.OfferResponse;
import com.cookerytech.dto.response.UsersOfferAndItemsResponse;
import com.cookerytech.service.OfferItemService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    @Mapping(source = "user.id", target = "userId"/*, ignore = true*/)
    @Mapping(source = "currency.id", target = "currencyId"/*, ignore = true*/)
    OfferDTO offerToOfferDTO(Offer offer);
    List<OfferDTO> offerListToOfferDTOList(List<Offer> offers);

//    @Mapping(target = "currency", ignore = true)
//    OfferDTO offerToOfferDTO(Offer offer);

    OfferResponse offerToOfferResponse(Offer offer);

    List<OfferResponse> offersToOfferResponses(List<Offer> offerList);

    @Mapping(target="id", ignore = true)
    Offer offerResponseToOffer(OfferResponse offerResponse);





}
