package com.cookerytech.service;

import com.cookerytech.domain.Product;
import com.cookerytech.domain.ProductPropertyKey;
import com.cookerytech.dto.ProductPropertyKeyDTO;
import com.cookerytech.dto.request.ProductPropertyRequest;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.ProductPropertyKeyMapper;
import com.cookerytech.repository.ProductPropertyKeyRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductPropertyKeyService {


    private final ProductPropertyKeyRepository productPropertyKeyRepository;
    private final ProductService productService;
    private final ProductPropertyKeyMapper productPropertyKeyMapper;

    private final ModelPropertyValueService modelPropertyValueService;

    public ProductPropertyKeyService(ProductPropertyKeyRepository productPropertyKeyRepository, @Lazy ProductService productService, ProductPropertyKeyMapper productPropertyKeyMapper, ModelPropertyValueService modelPropertyValueService) {
        this.productPropertyKeyRepository = productPropertyKeyRepository;
        this.productService = productService;
        this.productPropertyKeyMapper = productPropertyKeyMapper;
        this.modelPropertyValueService = modelPropertyValueService;
    }

    public ProductPropertyKeyDTO makeProductPropertyKey(ProductPropertyRequest createProductPropertyRequest) {
       ProductPropertyKey productPropertyKey2 = isThereNameAndProductId(createProductPropertyRequest.getName(),createProductPropertyRequest.getProductId());
        ProductPropertyKey productPropertyKey = new ProductPropertyKey();
        if (!(productPropertyKey2 instanceof ProductPropertyKey)){

            productPropertyKey.setSeq(createProductPropertyRequest.getSeq());
            productPropertyKey.setName(createProductPropertyRequest.getName());
            productPropertyKey.setProduct(productService.getById(createProductPropertyRequest.getProductId()));
            productPropertyKey.setBuiltIn(false);

        } else if(productPropertyKeyRepository.existsById(productPropertyKey2.getId())){
            throw new BadRequestException(ErrorMessage.PRODUCT_PROPERTY_KEY_NAME_EXIST_MESSAGE);
        }
        ProductPropertyKey productPropertyKey1 = productPropertyKeyRepository.save(productPropertyKey);
        return productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyDTO(productPropertyKey1);
    }


    public ProductPropertyKey getById(Long id){
        ProductPropertyKey productPropertyKey = productPropertyKeyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return productPropertyKey;
    }


    public ProductPropertyKeyDTO updateProductPropertyKey(Long id, ProductPropertyRequest productPropertyRequest) {
        ProductPropertyKey productPropertyKey = getById(id);

        if(productPropertyKey.getBuiltIn()){
            throw  new BadRequestException(ErrorMessage.CAN_NOT_UPDATE);
        }
        ProductPropertyKey productPropertyKey1 = isThereNameAndProductId(productPropertyRequest.getName(),productPropertyRequest.getProductId());
        if (productPropertyKey1 instanceof ProductPropertyKey){
            throw new BadRequestException(ErrorMessage.PRODUCT_PROPERTY_KEY_NAME_EXIST_MESSAGE);
        }
        productPropertyKey.setName(productPropertyRequest.getName());
        productPropertyKey.setProduct(productService.getById(productPropertyRequest.getProductId()));
        productPropertyKey.setSeq(productPropertyRequest.getSeq());

        productPropertyKeyRepository.save(productPropertyKey);
        return productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyDTO(productPropertyKey);
    }



    public List<ProductPropertyKeyDTO> getPropertyKeyByProductId(Long productId) {
        List<ProductPropertyKey> productPropertyKeys = productPropertyKeyRepository.findAllByProductId(productId);
        return   productPropertyKeyMapper.map(productPropertyKeys);
    }

    public List<Long> getPropertyKeyIdByProductId(Long productId) {
        List<ProductPropertyKey> productPropertyKeys = productPropertyKeyRepository.findAllByProductId(productId);
        return productPropertyKeys.stream().map(ppkey->ppkey.getId()).collect(Collectors.toList());
    }



    public ProductPropertyKeyDTO deleteProductPropertyKey(Long id) {  //A10

        ProductPropertyKey productPropertyKey = getById(id);

        //builtIn kontrolü
        if(productPropertyKey.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // A ProductPropertKey which has related records in ModelPropertyValues table can not be deleted.

        boolean exist = modelPropertyValueService.existByProductPropertyKey(productPropertyKey);
        if(exist) {
            throw  new BadRequestException(ErrorMessage.CAN_NOT_BE_DELETED_MESSAGE);
        }

        productPropertyKeyRepository.delete(productPropertyKey);

        return productPropertyKeyMapper.productPropertyKeyToProductPropertyKeyDTO(productPropertyKey);

    }

    public ProductPropertyKey isThereNameAndProductId(String name, Long productıd){
        return  productPropertyKeyRepository.findByProductPropertyKey(name, productıd);
    }

}
