package com.cookerytech.service;

import com.cookerytech.domain.*;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.ModelPropertyValueDTO;
import com.cookerytech.dto.ProductPropertyKeyDTO;
import com.cookerytech.dto.request.ProductPropertyRequest;
import com.cookerytech.dto.response.ModelByProductIdResponse;
import com.cookerytech.dto.response.ProductResponse;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.ModelMapper;
import com.cookerytech.repository.ProductRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.cookerytech.dto.ProductDTO;
import com.cookerytech.dto.request.ProductSaveRequest;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.mapper.ProductMapper;


import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPropertyKeyService productPropertyKeyService;
    private final ProductMapper productMapper;

    private final ModelService modelService;

    private final UserService userService;

    private final BrandService brandService;

    private final CategoryService categoryService;
    private final OfferItemService offerItemService;
    private final CartItemsService cartItemsService;
    private final FavoriteService favoriteService;
    private final RoleService roleService;
    private final ModelPropertyValueService modelPropertyValueService;
    private final ModelMapper modelMapper;


    public ProductService(ProductMapper productMapper, ProductRepository productRepository, ProductPropertyKeyService productPropertyKeyService, @Lazy ModelService modelService, UserService userService, @Lazy BrandService brandService, @Lazy CategoryService categoryService, OfferItemService offerItemService, @Lazy CartItemsService cartItemsService, @Lazy FavoriteService favoriteService, RoleService roleService, ModelPropertyValueService modelPropertyValueService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productPropertyKeyService = productPropertyKeyService;
        this.productMapper = productMapper;
        this.modelService = modelService;
        this.userService = userService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.offerItemService = offerItemService;
        this.cartItemsService = cartItemsService;
        this.favoriteService = favoriteService;
        this.roleService = roleService;
        this.modelPropertyValueService = modelPropertyValueService;
        this.modelMapper = modelMapper;
    }

    public Product getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
        return product;
    }

    public ProductPropertyKeyDTO makeProductProperty(ProductPropertyRequest createProductPropertyRequest) {
        return productPropertyKeyService.makeProductPropertyKey(createProductPropertyRequest);
    }

    public ProductPropertyKeyDTO updateProductProperty(Long id, ProductPropertyRequest productPropertyRequest) {
        return productPropertyKeyService.updateProductPropertyKey(id, productPropertyRequest);
    }

//    public ProductDTO deleteBrandById(Long id) {
//     Product product = getProduct(id);
//
//     if(product.getBuiltIn()){
//         throw  new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
//     }
//
//
//     productRepository.delete(product);
//
//    }

    private Product getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id))
        );
        return product;
    }

    @Transactional
    public ProductDTO deleteProductById(Long id) {
        Product product = getProduct(id);
        if (product.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        //offer-item iliskisi varsa silinemez
        Boolean existsOfferItemsByProductId = offerItemService.existsOfferItemsByProductId(id);
        if (existsOfferItemsByProductId) {
            throw new BadRequestException(ErrorMessage.CAN_NOT_BE_DELETED_MESSAGE);
        }
        List<Long> favoriteIds = favoriteService.getFavoritesByModelsOfProduct(id);
        for (Long favoriteId : favoriteIds) {
            favoriteService.deleteFavorite(favoriteId);
        }
        List<Long> modelIds = modelService.getModelIdsByProductId(id);

        for (Long modelId : modelIds) {
            modelService.deleteModelById(modelId);
        }

        List<Long> pPKeyIds = productPropertyKeyService.getPropertyKeyIdByProductId(id);
        for (Long pPKey : pPKeyIds) {
            productPropertyKeyService.deleteProductPropertyKey(pPKey);
        }

        // cart_items ve fqvorites içindeki ilgili kayıtlar silinmelidir.
        List<Long> cartItemsIds = cartItemsService.getCartItemsByProductId(id);
        for (Long cartItemId : cartItemsIds) {
            cartItemsService.deleteCartItem(cartItemId);
        }
        productRepository.delete(product);

        System.out.println(product.getId());

        return productMapper.productToProductDTO(product);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {

        List<Product> products = productRepository.getProductsByCategory(categoryId);

        return productMapper.map(products);
    }

    public ProductDTO saveProduct(ProductSaveRequest productSaveRequest) {


        String titleCumle = productSaveRequest.getTitle();
        titleCumle.replaceAll("[^a-zA-ZğüşıöçĞÜŞİÖÇ\\s]", "-").toLowerCase(); // title -> Kahve Makinesi
        // sluq  -> kahve-makinesi

        //Product product = productMapper.productSaveRequestToProduct(productSaveRequest);

        Product product = new Product();

        Brand brand = brandService.getBrand(productSaveRequest.getBrandId());

        Category category = categoryService.getCategory(productSaveRequest.getCategoryId());


        product.setTitle(productSaveRequest.getTitle());
        product.setShortDesc(productSaveRequest.getShortDesc());
        product.setLongDesc(productSaveRequest.getLongDesc());
        product.setSeq(productSaveRequest.getSeq());
        product.setIsNew(productSaveRequest.getIsNew());
        product.setIsFeatured(productSaveRequest.getIsFeatured());
        product.setIsActive(productSaveRequest.getIsActive());
        product.setBrand(brand);
        product.setCategory(category);
        product.setCreateAt(LocalDateTime.now());
        product.setSlug(titleCumle);

        Product createProduct = productRepository.save(product);

        return productMapper.productToProductDTO(createProduct);

    }

    public ProductDTO updateProductId(Long id, ProductSaveRequest productSaveRequest) {

        Product product = getProduct(id);

        if (product.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        LocalDateTime now = LocalDateTime.now();

        product.setTitle(productSaveRequest.getTitle());
        product.setShortDesc(productSaveRequest.getShortDesc());
        product.setLongDesc(productSaveRequest.getLongDesc());
        product.setSeq(productSaveRequest.getSeq());
        product.setIsNew(productSaveRequest.getIsNew());
        product.setIsFeatured(productSaveRequest.getIsFeatured());
        product.setIsActive(productSaveRequest.getIsActive());
        product.setSlug(product.getSlug());
        product.setBrand(product.getBrand());
        product.setCategory(product.getCategory());
        product.setUpdateAt(now);

        Product updateProduct = productRepository.save(product);

        return productMapper.productToProductDTO(updateProduct);

    }


    public List<Product> getProductByBrandId(Long brandId) {

        List<Product> productList = productRepository.findProductByBrandId(brandId);

        return productList;
    }
//    public ProductDTO updateProductId(Long id, ProductSaveRequest productSaveRequest) {
//
//        Product product = getProduct(id);
//
//        if(product.getBuiltIn()){
//            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//
//        product.setTitle(productSaveRequest.getTitle());
//        product.setShortDesc(productSaveRequest.getShortDesc());
//        product.setLongDesc(productSaveRequest.getLongDesc());
//        product.setSeq(productSaveRequest.getSeq());
//        product.setIsNew(productSaveRequest.getIsNew());
//        product.setIsFeatured(productSaveRequest.getIsFeatured());
//        product.setIsActive(productSaveRequest.getIsActive());
//        product.setSlug(product.getSlug());
//        product.setBrands(product.getBrands());
//        product.setCategory(product.getCategory());
//        product.setUpdateAt(now);
//
//        Product updateProduct = productRepository.save(product);
//
//        return productMapper.productToProductDTO(updateProduct);
//
//    }

    @Transactional
    public List<ModelByProductIdResponse>       getModelsByProductId(Long productId) {


        List<ModelByProductIdResponse> modelByProductIdResponseList = new ArrayList<>();

        if ( SecurityContextHolder.getContext().getAuthentication().getPrincipal()=="anonymousUser") {
            List<Model> models = modelService.getModelsByProductIdActiveModelBrandCategoryProduct(productId);
            for (Model model : models) {
                ModelByProductIdResponse modelByProductIdResponse = new ModelByProductIdResponse();
                List<ModelPropertyValue> modelPropertyValueList = modelPropertyValueService.getModelPropertyValueByModel(model);
                modelByProductIdResponse.setModelDTO(modelMapper.modelToModelDTO(model));
                modelByProductIdResponse.setModelPropertyValueDTOSList(
                        modelPropertyValueList.stream().map(modelPropertyValue ->
                                new ModelPropertyValueDTO(modelPropertyValue)).collect(Collectors.toList())
                );
                modelByProductIdResponseList.add(modelByProductIdResponse);
            }
        } else {
            User user = userService.getCurrentUser();
            Set<Role> userRole = user.getRoles();

            if (!userRole.contains(roleService.findByType(RoleType.ROLE_ADMIN))) {
                List<Model> models = modelService.getModelsByProductIdActiveModelBrandCategoryProduct(productId);
                for (Model model : models) {
                    ModelByProductIdResponse modelByProductIdResponse = new ModelByProductIdResponse();
                    List<ModelPropertyValue> modelPropertyValueList = modelPropertyValueService.getModelPropertyValueByModel(model);
                    modelByProductIdResponse.setModelDTO(modelMapper.modelToModelDTO(model));
                    modelByProductIdResponse.setModelPropertyValueDTOSList(
                            modelPropertyValueList.stream().map(modelPropertyValue ->
                                    new ModelPropertyValueDTO(modelPropertyValue)).collect(Collectors.toList())
                    );
                    modelByProductIdResponse.setIsFavorite(favoriteService.isFavoriteByModelAndCurrentlyUser(model, user));
                    modelByProductIdResponseList.add(modelByProductIdResponse);
                }
            } else {
                List<Model> models = modelService.getModelsByProductId(productId);
                for (Model model : models) {
                    ModelByProductIdResponse modelByProductIdResponse = new ModelByProductIdResponse();
                    List<ModelPropertyValue> modelPropertyValueList = modelPropertyValueService.getModelPropertyValueByModel(model);
                    modelByProductIdResponse.setModelDTO(modelMapper.modelToModelDTO(model));
                    modelByProductIdResponse.setModelPropertyValueDTOSList(
                            modelPropertyValueList.stream().map(modelPropertyValue ->
                                    new ModelPropertyValueDTO(modelPropertyValue)).collect(Collectors.toList())

                    );
                    modelByProductIdResponse.setIsFavorite(favoriteService.isFavoriteByModelAndCurrentlyUser(model, user));
                    modelByProductIdResponseList.add(modelByProductIdResponse);
                }
            }
        }
        return modelByProductIdResponseList;
    }

    public List<ProductPropertyKeyDTO> getPropertyKeyByProductId(Long id) {

        List<ProductPropertyKeyDTO> productPropertyKeyDTOS = productPropertyKeyService.getPropertyKeyByProductId(id);
        return productPropertyKeyDTOS;

    }


    public ProductPropertyKeyDTO deleteProductPropertyById(Long id) {  //A10
        return productPropertyKeyService.deleteProductPropertyKey(id);
    }

    public Page<ProductDTO> getProductDTOPage(String q, Pageable pageable) {


        Page<Product> adminProductPage = productRepository.findAll(pageable);

        return adminProductPage.map(brand -> productMapper.productToProductDTO(brand));

/*
            Set<Role> userRole = userService.getCurrentUser().getRoles();

            if (!userRole.contains(roleService.findByType(RoleType.ROLE_ADMIN))) {  //roleService.findByType(RoleType.ROLE_ADMIN)

                Page<Product> productPage = productRepository.getActiveProducts(q, pageable);

                return productPage.map(brand -> productMapper.productToProductDTO(brand));
            }
            Page<Product> adminProductPage = productRepository.findAll(pageable);

            return adminProductPage.map(brand -> productMapper.productToProductDTO(brand));

 */


    }

    public List<ProductResponse> getAllFeaturedProducts() {

        List<Product> adminProductList = productRepository.getAllFeaturedProductsForAdmin();

        return productMapper.productToProductResponse(adminProductList);


        /*

        List<Product> productList = productRepository.getAllFeaturedProducts();

        User authenticatedUser = userService.getCurrentUser();

        if (authenticatedUser.getRoles().contains(roleService.findByType(RoleType.ROLE_ADMIN))) {

            List<Product> adminProductList = productRepository.getAllFeaturedProductsForAdmin();

            return productMapper.productToProductResponse(adminProductList);
        }

        return productMapper.productToProductResponse(productList);

         */

    }

    public ProductDTO getProductById(Long id) {

        Product product = getProduct(id);

        return productMapper.productToProductDTO(product);

    }

    public long getNumberOfProducts() {
        return productRepository.numberOfPublishedProduct();

    }

    public List<ProductDTO> getProductsNoOffer() { //G04

        List<Product> productsNoOffer = productRepository.getProductsNoOffer();

        return productMapper.map(productsNoOffer);

    }

    public List<ProductDTO> getMostPopularProducts(int amount) {

        List<Product> mostPopularProducts = productRepository.getMostPopularProducts();
        List<Product> amountProducts = mostPopularProducts.stream().limit(amount).collect(Collectors.toList());

        return productMapper.map(amountProducts);
    }

    public Boolean isThereProduct(Long id) {
        return productRepository.existsById(id);
    }

//    public Double getBrandByProductId(Long productId) {
//        Product product = getProduct(productId);
//        Double profitRates = product.getBrand().getProfitRate();
//        return profitRates;
//    }
}
