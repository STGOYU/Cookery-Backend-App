package com.cookerytech.dto.response;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.ModelPropertyValue;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.ModelPropertyValueDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelByProductIdResponse {

    private ModelDTO modelDTO;

    private List<ModelPropertyValueDTO> modelPropertyValueDTOSList;

    private Boolean isFavorite=false;
}
