package com.cookerytech.controller;

import com.cookerytech.dto.ModelDTO;
import com.cookerytech.dto.request.ModelCreatRequest;
import com.cookerytech.dto.request.ModelUpdateRequest;
import com.cookerytech.dto.response.ModelResponse;
import com.cookerytech.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelDTO> updateModelById(@PathVariable("id") Long id,
                                                    @Valid  @ModelAttribute ModelUpdateRequest modelUpdateRequest){
        ModelDTO modelDTO = modelService.updateModelById(id, modelUpdateRequest);
        return ResponseEntity.ok(modelDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelDTO> deleteModelById(@PathVariable("id") Long id){
        ModelDTO modelDTO = modelService.deleteModelById(id);
        return ResponseEntity.ok(modelDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<ModelDTO> creatModel(@Valid @ModelAttribute ModelCreatRequest modelCreatRequest){
        ModelDTO createdModel= modelService.creatModel(modelCreatRequest);
        return ResponseEntity.ok(createdModel);
    }




}
