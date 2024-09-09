package com.cookerytech.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String title;
    private String description;
    private Boolean builtIn = false ;
    private Integer seq = 0;
    private String slug;
    private Boolean isActive = true;
    private Integer mainCategoryId;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
}