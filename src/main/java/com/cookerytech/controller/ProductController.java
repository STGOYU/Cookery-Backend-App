package com.cookerytech.controller;

import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.ProductPropertyKeyDTO;
import com.cookerytech.dto.request.ProductPropertyRequest;
import com.cookerytech.dto.response.ModelByProductIdResponse;
import com.cookerytech.dto.response.ProductResponse;
import com.cookerytech.service.ProductService;

import com.cookerytech.dto.ProductDTO;
import com.cookerytech.service.ProductService;
import com.cookerytech.dto.request.ProductSaveRequest;
import com.cookerytech.dto.response.CTResponse;
import com.cookerytech.dto.response.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductDTO> createProduct(
            @Valid @RequestBody ProductSaveRequest productSaveRequest){
        ProductDTO createProduct = productService.saveProduct(productSaveRequest);
        return ResponseEntity.ok(createProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductSaveRequest productSaveRequest){

        ProductDTO updateProductDTO = productService.updateProductId(id,productSaveRequest);
        return ResponseEntity.ok(updateProductDTO);

    }

    @PostMapping("/properties")         //Sayfa 33 -> A08
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyDTO> makeProductProperty(@Valid @RequestBody ProductPropertyRequest createProductPropertyRequest){
            ProductPropertyKeyDTO productPropertyKeyDTO = productService.makeProductProperty(createProductPropertyRequest);
            return ResponseEntity.ok(productPropertyKeyDTO);
    }

    @PutMapping("/properties/{id}")    //Sayfa 34 -> A09
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyDTO> updateProductProperty(@PathVariable("id") Long id, @Valid @RequestBody ProductPropertyRequest productPropertyRequest){
        ProductPropertyKeyDTO productPropertyKeyDTO = productService.updateProductProperty(id, productPropertyRequest);
        return ResponseEntity.ok(productPropertyKeyDTO);
    }

    @DeleteMapping("/properties/{id}")  //A10
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductPropertyKeyDTO> deleteProductProperty(@PathVariable Long id){
        ProductPropertyKeyDTO deletedProductPropertyKey = productService.deleteProductPropertyById(id);
        return ResponseEntity.ok(deletedProductPropertyKey);
    }


    @DeleteMapping("/{id}/admin")//Sayfa 31 A06
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id){
        ProductDTO deletedProduct =  productService.deleteProductById(id);
        return  ResponseEntity.ok(deletedProduct);
    }

    @GetMapping("/{id}/properties")    //Sayfa 32 -> A07
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ProductPropertyKeyDTO>> getPropertyKeyByProductId(@PathVariable Long id){

        List<ProductPropertyKeyDTO> productPropertyKeyDTOS = productService.getPropertyKeyByProductId(id);

        return  ResponseEntity.ok(productPropertyKeyDTOS);

    }

    @GetMapping("/{productId}/models") //A11
    public ResponseEntity<List<ModelByProductIdResponse>> getModelsByProductId(@PathVariable Long productId){

        List<ModelByProductIdResponse> modelByProductIdResponseList= productService.getModelsByProductId(productId);
        return ResponseEntity.ok(modelByProductIdResponseList);

    }

    @GetMapping("/auth") // A01
    //@PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Page<ProductDTO>> getProductsAsPages(@RequestParam(value = "q", defaultValue = "") String q,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "20") int size,
                                                               @RequestParam(value = "sort", defaultValue = "category_id") String prop,
                                                               @RequestParam(value = "type", defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        Page<ProductDTO> productDTOPage = productService.getProductDTOPage(q.toLowerCase(), pageable);

        return ResponseEntity.ok(productDTOPage);
    }

    @GetMapping("/featured") //A02
    //@PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts(){

        List<ProductResponse> productResponse = productService.getAllFeaturedProducts();

        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{id}") //A03
    //@PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<ProductDTO> getProductsById (@PathVariable("id") Long id){

        ProductDTO productDTO = productService.getProductById(id);

        return ResponseEntity.ok(productDTO);

    }



}
