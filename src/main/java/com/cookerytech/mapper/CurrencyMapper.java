package com.cookerytech.mapper;


import com.cookerytech.domain.Currency;
import com.cookerytech.dto.CurrencyDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CurrencyMapper {

    CurrencyDTO currencyToCurrencyDTO(Currency currency);

    default Page<CurrencyDTO> currencyPageToCurrencyDTOPage(Page<Currency> currencyPage) {
        return currencyPage.map(this::currencyToCurrencyDTO);
    }

}
