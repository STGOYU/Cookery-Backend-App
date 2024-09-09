package com.cookerytech.mapper;

import com.cookerytech.domain.Category;
import com.cookerytech.dto.CategoryDTO;
import com.cookerytech.dto.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {

    CategoryDTO categoryToCategoryDTO(Category category);

    Category categorySaveRequestToCategory(CategoryRequest categoryRequest);

    List<CategoryDTO> categoryListToCategoryDTOList(List<Category> categoryList);
}