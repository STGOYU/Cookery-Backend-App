package com.cookerytech.controller;

import com.cookerytech.dto.BrandDTO;
import com.cookerytech.dto.CategoryDTO;
import com.cookerytech.dto.ProductDTO;
import com.cookerytech.dto.request.BrandRequest;
import com.cookerytech.dto.request.BrandSaveRequest;
import com.cookerytech.dto.request.CategoryRequest;
import com.cookerytech.dto.request.CategoryUpdateRequest;
import com.cookerytech.dto.response.CTResponse;
import com.cookerytech.dto.response.ResponseMessage;
import com.cookerytech.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //B06
    @GetMapping("/{categoryId}/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER') or " +
            " hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<ProductDTO>>  getProductsByCategory(@PathVariable Long categoryId){

        List<ProductDTO>  productDTOList = categoryService.getProductsByCategory(categoryId);

        return ResponseEntity.ok(productDTOList);
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest){

        CategoryDTO create =  categoryService.saveCategory(categoryRequest);

        return ResponseEntity.ok(create);
    }

    //B04
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryDTO>  updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        CategoryDTO updatedCategoryDTO =  categoryService.updateCategoryWithId(id, categoryUpdateRequest);
        return  ResponseEntity.ok(updatedCategoryDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<CategoryDTO> deleteCategoryByID(@PathVariable Long id){

        CategoryDTO deleteCategoryDTO = categoryService.deleteCategoryByID(id);
        return ResponseEntity.ok(deleteCategoryDTO);

    }

    //B02
    @GetMapping("/{id}")

    public ResponseEntity<CategoryDTO>  getCategoryByID(@PathVariable Long id){

        CategoryDTO categoryDTO = categoryService.getCategoryByID(id);

        return ResponseEntity.ok(categoryDTO);
    }

    //B01
    @GetMapping("")

    public ResponseEntity<List<CategoryDTO>>  getAllCategories(){

        List<CategoryDTO>  categoryDTOList = categoryService.getAllCategories();

        return ResponseEntity.ok(categoryDTOList);
    }

}
