package com.cookerytech.controller;

import com.cookerytech.domain.Image;
import com.cookerytech.dto.response.CTResponse;
import com.cookerytech.dto.response.ImageSavedResponse;
import com.cookerytech.dto.response.ResponseMessage;
import com.cookerytech.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }



    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<ImageSavedResponse> uploadFile(
            @RequestParam("file") MultipartFile file){

        String imageId = imageService.saveImage(file);

        ImageSavedResponse response = new ImageSavedResponse(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE,true,imageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<byte[]> displayFile(@PathVariable String id){
        Image image = imageService.getImageById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(image.getImageData().getData(),
                header,
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<CTResponse> deleteImageFile(@PathVariable String id){
        imageService.removeById(id);

        CTResponse response = new CTResponse(ResponseMessage.IMAGE_DELETED_RESPONSE_MESSAGE, true);
        return ResponseEntity.ok(response);
    }



}
