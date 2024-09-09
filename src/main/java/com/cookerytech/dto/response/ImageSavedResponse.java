package com.cookerytech.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageSavedResponse extends CTResponse{

    private String imageId;

    public ImageSavedResponse(String message, boolean success, String imageId) {
        super(message, success);
        this.imageId = imageId;
    }
}
