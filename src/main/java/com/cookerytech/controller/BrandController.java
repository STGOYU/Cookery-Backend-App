package com.cookerytech.controller;


import com.cookerytech.dto.BrandDTO;
import com.cookerytech.dto.request.BrandRequest;
import com.cookerytech.dto.BrandDTO;
import com.cookerytech.dto.request.BrandSaveRequest;
import com.cookerytech.dto.response.CTResponse;
import com.cookerytech.dto.response.ResponseMessage;
import com.cookerytech.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;


    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandDTO> createBrand(
            @Valid @RequestBody BrandSaveRequest brandSaveRequest){
        BrandDTO createBrand =  brandService.saveBrand(brandSaveRequest);
        return ResponseEntity.ok(createBrand);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandDTO>  updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest){
      BrandDTO updatedBrandDTO =  brandService.updateBrandById(id,brandRequest);
        return  ResponseEntity.ok(updatedBrandDTO);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<BrandDTO>  deleteBrand(@PathVariable Long id){
      BrandDTO deletedBrand=  brandService.deleteBrandById(id);
        return  ResponseEntity.ok(deletedBrand);
    }

    @GetMapping()
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Page<BrandDTO>> getBrandsForPM(@RequestParam (value = "page", defaultValue = "0") int page,
                                                    @RequestParam (value = "size", defaultValue = "20") int size,
                                                    @RequestParam (value = "sort", defaultValue = "name") String prop,
                                                    @RequestParam (value = "type", defaultValue = "ASC") Sort.Direction direction){

        Pageable pageable = PageRequest.of(page, size,Sort.by(direction,prop));

        Page<BrandDTO> brandDTOPage = brandService.getBrandDTOPage(pageable);

        return ResponseEntity.ok(brandDTOPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<BrandDTO> getBrandDTOByIdForPM(@PathVariable("id") Long id){

        BrandDTO getBrandDTOById = brandService.getBrandDTOById(id);

        return ResponseEntity.ok(getBrandDTOById);

    }

}
