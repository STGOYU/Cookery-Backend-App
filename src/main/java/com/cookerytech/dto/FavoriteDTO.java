package com.cookerytech.dto;

import com.cookerytech.domain.Favorite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {

    private Long id;


    private ModelDTO modelDTO;


    private Long userId;


    private LocalDateTime createAt;

    public FavoriteDTO(Favorite favorite, ModelDTO modelDTO) {
        this.id = favorite.getId();
        this.createAt = favorite.getCreateAt();
        this.userId = favorite.getUser().getId();
        this.modelDTO = modelDTO;
//        this.productDTO.setId(favorite.getProduct().getId());
//        this.productDTO.setTitle(favorite.getProduct().getTitle());
//        this.productDTO.setShortDesc(favorite.getProduct().getShortDesc());
//        this.productDTO.setLongDesc(favorite.getProduct().getLongDesc());
//        this.productDTO.setSlug(favorite.getProduct().getSlug());
//        this.productDTO.setSeq(favorite.getProduct().getSeq());
//        this.productDTO.setIsNew(favorite.getProduct().getIsNew());
//        this.productDTO.setIsFeatured(favorite.getProduct().getIsFeatured());
//        this.productDTO.setIsActive(favorite.getProduct().getIsActive());
//        this.productDTO.setBuiltIn(favorite.getProduct().getBuiltIn());
//        this.productDTO.setBuiltIn(favorite.getProduct().getBuiltIn());
//        this.productDTO.setBrandIds(favorite.getProduct().getBrands().stream().map(b));


    }
}
