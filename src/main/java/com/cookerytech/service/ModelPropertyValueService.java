package com.cookerytech.service;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.ModelPropertyValue;
import com.cookerytech.domain.ProductPropertyKey;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.repository.ModelPropertyValueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelPropertyValueService {

    private final ModelPropertyValueRepository modelPropertyValueRepository;


    public ModelPropertyValueService(ModelPropertyValueRepository modelPropertyValueRepository) {
        this.modelPropertyValueRepository = modelPropertyValueRepository;
    }
    public boolean existByProductPropertyKey(ProductPropertyKey productPropertyKey) {  //A10
        return  modelPropertyValueRepository.existsByProductPropertyKey(productPropertyKey);
    }

    public void deleteModelPropertyValue(Long modelPropertyValueId){
        getModelPropertyValue(modelPropertyValueId);
           modelPropertyValueRepository.deleteById(modelPropertyValueId);

    }
    private ModelPropertyValue getModelPropertyValue(Long modelPropertyValueId){
     return    modelPropertyValueRepository.findById(modelPropertyValueId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,modelPropertyValueId)));
    }

    public List<Long> getModelPropertyValueIdByModel(Model model) {
      List<ModelPropertyValue> modelPropertyValues =  modelPropertyValueRepository.findAllByModel(model);
      return  modelPropertyValues.stream().map(mPValue->mPValue.getId()).collect(Collectors.toList());
    }
    public List<ModelPropertyValue> getModelPropertyValueByModel(Model model) {

      return  modelPropertyValueRepository.findAllByModel(model);
    }
}
