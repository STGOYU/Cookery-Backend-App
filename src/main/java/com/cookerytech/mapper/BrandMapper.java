package com.cookerytech.mapper;

import com.cookerytech.domain.Brand;
import com.cookerytech.dto.BrandDTO;
import com.cookerytech.dto.request.BrandSaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BrandMapper {


    BrandDTO brandToBrandDTO(Brand brand);


    Brand brandSaveRequestToBrand(BrandSaveRequest brandSaveRequest);
}
