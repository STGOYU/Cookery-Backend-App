package com.cookerytech.service;

import com.cookerytech.domain.*;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.request.ModelCreatRequest;
import com.cookerytech.dto.request.ModelUpdateRequest;
import com.cookerytech.dto.response.ModelResponse;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ConflictException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.ModelMapper;
import com.cookerytech.repository.ModelRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    private final CurrencyService currencyService;

    private  final  ImageService imageService;

    private  final  OfferItemService offerItemService;

    private  final  ModelPropertyValueService modelPropertyValueService;

    @Lazy
    private  final CartItemsService cartItemsService;
    private  final  FavoriteService favoriteService;

    public ModelService(ModelRepository modelRepository, ModelMapper modelMapper,@Lazy  ProductService productService, CurrencyService currencyService, ImageService imageService,@Lazy  OfferItemService offerItemService, ModelPropertyValueService modelPropertyValueService, @Lazy CartItemsService cartItemsService, @Lazy FavoriteService favoriteService) {
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.currencyService = currencyService;
        this.imageService = imageService;
        this.offerItemService = offerItemService;
        this.modelPropertyValueService = modelPropertyValueService;
        this.cartItemsService = cartItemsService;
        this.favoriteService = favoriteService;
    }

    public Model getModelById(Long id){
       return modelRepository.findById(id).
               orElseThrow(()-> new RuntimeException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION, id)));
    }
    @Transactional
    public ModelDTO updateModelById(Long id, ModelUpdateRequest modelUpdateRequest) {
        Model model = getModelById(id);

        if (model.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        Boolean existsBySku= modelRepository.existsBySku(modelUpdateRequest.getSku());

        if(existsBySku && !(model.getSku().equals(modelUpdateRequest.getSku()))) {
            throw new ConflictException(ErrorMessage.SKU_ALREADY_EXİST);
        }
       String imgId = imageService.saveImage2(modelUpdateRequest.getImage());
      Image savedImg =   imageService.getImageById(imgId);
      Set<Image> imageSet = model.getImages();
      imageSet.add(savedImg);



        model.setTitle(modelUpdateRequest.getTitle());
        model.setSku(modelUpdateRequest.getSku());
        model.setStockAmount(modelUpdateRequest.getStockAmount());
        model.setInBoxQuantity(modelUpdateRequest.getInBoxQuantity());
        model.setSeq(modelUpdateRequest.getSeq());
        model.setBuyingPrice(modelUpdateRequest.getBuyingPrice());
        model.setTaxRate(modelUpdateRequest.getTaxRate());
        model.setIsActive(modelUpdateRequest.getIsActive());
        model.setUpdateAt(LocalDateTime.now());
        model.setImages(imageSet);
        model.setProduct(productService.getById(modelUpdateRequest.getProductId()));
        model.setCurrency(currencyService.getCurrencyById(modelUpdateRequest.getCurrencyId()));

        modelRepository.save(model);

        return modelMapper.modelToModelDTO(model);

    }


    public ModelDTO deleteModelById(Long id) {
        Model model = getModelById(id);
        if (model.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        // ??  If the model has any related records in offer_items table,
        Boolean  existsOfferItemsByModel =  offerItemService.existsOfferItemsByModel(model);
        if(existsOfferItemsByModel){
            throw new BadRequestException(ErrorMessage.CAN_NOT_BE_DELETED_MESSAGE);
        }

        // ?? – If any model is deleted, related records in model_property_values, cart_items should be deleted.

        List<Long> mPValueId = modelPropertyValueService.getModelPropertyValueIdByModel(model);
        for (Long mPV:mPValueId){
            modelPropertyValueService.deleteModelPropertyValue(mPV);
        }
          List<Long> cartItemId = cartItemsService.getCartItemIdsByModel(model);
        for (Long cIId:cartItemId){
           cartItemsService.deleteCartItem(cIId);
        }
        List<Long> favoriteIds = favoriteService.getFavoriteIdsByModel(model);
        for (Long fId:favoriteIds){
           favoriteService.deleteFavorite(fId);
        }
        modelRepository.delete(model);
        return modelMapper.modelToModelDTO(model);

    }

    public ModelDTO creatModel(ModelCreatRequest modelCreatRequest) {
        Boolean existsBySku= modelRepository.existsBySku(modelCreatRequest.getSku());

        if(existsBySku) {
            throw new ConflictException(ErrorMessage.SKU_ALREADY_EXİST);
        }
        LocalDateTime now = LocalDateTime.now();

       Set<String> imageIds = modelCreatRequest.getImages().stream().map(img->imageService.saveImage2(img)).collect(Collectors.toSet());
       Set<Image> savedImages = imageIds.stream().map(id->imageService.getImageById(id)).collect(Collectors.toSet());
        Currency currency=currencyService.getCurrencyById(modelCreatRequest.getCurrencyId());
        //Gelen productId ye göre product getirilecek
        Product product= productService.getById(modelCreatRequest.getProductId());

        Model model=new Model();
        model.setTitle(modelCreatRequest.getTitle());
        model.setSku(modelCreatRequest.getSku());
        model.setStockAmount(modelCreatRequest.getStockAmount());
        model.setInBoxQuantity(modelCreatRequest.getInBoxQuantity());
        model.setSeq(model.getSeq());
        model.setImages(savedImages);
        model.setBuyingPrice(modelCreatRequest.getBuyingPrice());
        model.setTaxRate(modelCreatRequest.getTaxRate());
        model.setIsActive(modelCreatRequest.getIsActive());
        model.setCurrency(currency);
        model.setProduct(product);
        model.setCreateAt(now);
        Model savedModel= modelRepository.save(model);
        return modelMapper.modelToModelDTO(savedModel);



    }


    public Page<ModelDTO> getModelDTOPage(Pageable pageable) {

        Page<Model> modelPage = modelRepository.getActiveModels(pageable);

        return modelPage.map(model -> modelMapper.modelToModelDTO(model));

    }

    public List<Model> getModelsByProductId(Long productId) {
        return modelRepository.findAllByProductId(productId);

    }

    @Transactional
    public List<Long> getModelIdsByProductId(Long productId) {
        List<Model> modelList= modelRepository.findAllByProductId(productId);
        return modelList.stream().map(model -> model.getId()).collect(Collectors.toList());
    }

    public List<Model> getModelsByProductIdActiveModelBrandCategoryProduct(Long productId) {
        return      modelRepository.getModelsByProductIdActiveModelBrandCategoryProduct(productId);
    }


}


