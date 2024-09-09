package com.cookerytech.service;

import com.cookerytech.domain.ImageData;
import com.cookerytech.domain.Image;
import com.cookerytech.domain.Model;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }



    public String saveImage(MultipartFile file) {

        Image image = null;
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));


        try{
            ImageData imageData = new ImageData(file.getBytes());
            //image = new Image(filename, file.getContentType(), imageData);
            image = new Image(filename,file.getContentType(), 1L, imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Image saveImage = imageRepository.save(image);
        return saveImage.getId();

    }
    public String saveImage2(MultipartFile file) {

        Image image = null;
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));


        try{
            ImageData imageData = new ImageData(file.getBytes());

            image = new Image(filename,file.getContentType(), imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        Image saveImage = imageRepository.save(image);
        return saveImage.getId();

    }

    public Image getImageById(String id) {

        Image image = imageRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE,id)));

        return image;
    }

    public void removeById(String id) {
        Image image = getImageById(id);
        imageRepository.delete(image);
    }
}
