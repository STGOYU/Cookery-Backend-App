package com.cookerytech.service;

import com.cookerytech.domain.Brand;
import com.cookerytech.domain.Product;
import com.cookerytech.domain.Role;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.dto.BrandDTO;
import com.cookerytech.dto.request.BrandRequest;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.BrandMapper;
import com.cookerytech.dto.request.BrandSaveRequest;
import com.cookerytech.repository.BrandRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final ProductService productService;

    private final UserService userService;

    private final RoleService roleService;



    public BrandService(BrandRepository brandRepository, BrandMapper brandMapper, ProductService productService,
                        UserService userService,RoleService roleService) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.productService = productService;
        this.userService = userService;
        this.roleService = roleService;
    }

    public BrandDTO saveBrand(BrandSaveRequest brandSaveRequest) {

        // Mapper Islemi -> brandSaveRequest to Brand
        Brand brand = brandMapper.brandSaveRequestToBrand(brandSaveRequest);

        // Olusturulma zamanini setledik.
        brand.setCreateAt(LocalDateTime.now());

        Brand updateBrand = brandRepository.save(brand);

        return brandMapper.brandToBrandDTO(updateBrand);



    }

    public BrandDTO updateBrandById(Long id, BrandRequest brandRequest) {
      Brand brand = getBrand(id);

      if (brand.getBuiltIn()){
          throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
      }

        LocalDateTime now = LocalDateTime.now();

      brand.setName(brandRequest.getName());
      brand.setProfitRate(brandRequest.getProfitRate());
      brand.setIsActive(brandRequest.getIsActive());
      brand.setBuiltIn(brandRequest.getBuiltIn());
      brand.setCreateAt(brand.getCreateAt());
      brand.setUpdateAt(now);
        Brand updatedBrand= brandRepository.save(brand);


      return  brandMapper.brandToBrandDTO(updatedBrand);
    }

    public BrandDTO deleteBrandById(Long id) {

        Brand brand = getBrand(id);

        if (brand.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

       List<Product> productList=productService.getProductByBrandId(id);

        if(productList.size()>0){
            throw new BadRequestException(ErrorMessage.BRAND_CAN_NOT_DELETED);
        }

        brandRepository.delete(brand);

        return  brandMapper.brandToBrandDTO(brand);


    }
    public Brand getBrand(Long id){
        Brand brand = brandRepository.findById(id).orElseThrow(()->
             new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id))
        );
        return brand;
    }


    public Page<BrandDTO> getBrandDTOPage(Pageable pageable) {

        Page<Brand> brandPage = brandRepository.getActiveBrands(pageable);

        Page<Brand> adminBrandPage = brandRepository.findAll(pageable);

        Set<Role> userRole = userService.getCurrentUser().getRoles();


        if (userRole.contains(roleService.findByType(RoleType.ROLE_PRODUCT_MANAGER))) {

            return brandPage.map(brand -> brandMapper.brandToBrandDTO(brand));
        }

        return adminBrandPage.map(brand -> brandMapper.brandToBrandDTO(brand));

    }

    public BrandDTO getBrandDTOById(Long id) {

        Brand brand = getBrand(id);

        Set<Role> userRole = userService.getCurrentUser().getRoles();

        if(userRole.contains(roleService.findByType(RoleType.ROLE_PRODUCT_MANAGER))){
            if(!brand.getIsActive()){
            throw new ResourceNotFoundException(String.format(ErrorMessage.NO_ACTIVE_BRANDS_MESSAGE,id));
        }
            return brandMapper.brandToBrandDTO(brand);

        }

//        else{
 //           return brandMapper.brandToBrandDTO(brand);

 //       }
        return brandMapper.brandToBrandDTO(brand);
    }


    public long getNumberOfBrands() {
       return brandRepository.numberOfPublishedBrand();
    }
}
