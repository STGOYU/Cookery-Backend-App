package com.cookerytech.service;

import com.cookerytech.domain.Category;
import com.cookerytech.domain.Role;
import com.cookerytech.domain.User;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.dto.CategoryDTO;
import com.cookerytech.dto.ProductDTO;
import com.cookerytech.dto.request.CategoryRequest;
import com.cookerytech.dto.request.CategoryUpdateRequest;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.CategoryMapper;
import com.cookerytech.repository.CategoryRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final ProductService productService;

    private final CategoryMapper categoryMapper;

    private final UserService userService;


    public CategoryService(CategoryRepository categoryRepository, ProductService productService, CategoryMapper categoryMapper, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.categoryMapper = categoryMapper;
        this.userService = userService;
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {

        List<ProductDTO> productDTOList = productService.getProductsByCategory(categoryId);
        return productDTOList;

    }

    public Category getCategory(Long id) {

        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

    }

    public CategoryDTO saveCategory(CategoryRequest categoryRequest) {
        Optional<Category> existingCategory = categoryRepository.findByTitle(categoryRequest.getTitle());

        if (existingCategory.isPresent()) {
            throw new ResourceNotFoundException(ErrorMessage.CATEGORY_ALREADY_EXIST_MESSAGE);
        }

        String titleCumle = categoryRequest.getTitle();
        titleCumle.replaceAll("[^a-zA-ZğüşıöçĞÜŞİÖÇ\\s]", "-").toLowerCase();


        Category category = categoryMapper.categorySaveRequestToCategory(categoryRequest);

        category.setCreateAt(LocalDateTime.now());
        category.setMainCategoryId(3);
        long seqNo = categoryRepository.count() + 1;
        category.setSeq((int) seqNo);
        category.setBuiltIn(false);
        category.setDescription("Category aciklama");
        category.setSlug(titleCumle);
        category.setTitle(categoryRequest.getTitle());


        categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(category);
    }

    public CategoryDTO updateCategoryWithId(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = getCategory(id);

        if (category.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        String titleCumle = categoryUpdateRequest.getTitle();
        titleCumle.replaceAll("[^a-zA-ZğüşıöçĞÜŞİÖÇ\\s]", "-").toLowerCase();

        category.setTitle(categoryUpdateRequest.getTitle());
        category.setIsActive(categoryUpdateRequest.getIsActive());
        category.setSlug(titleCumle);
        category.setUpdateAt(LocalDateTime.now());

        Category updateCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(updateCategory);
    }

    public CategoryDTO deleteCategoryByID(Long id) {

        Category category = getCategory(id);

        if (category.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        List<ProductDTO> productDTOList = getProductsByCategory(id);

        if (productDTOList.size() > 0) {
            throw new BadRequestException(ErrorMessage.CATEGORY_CANNOT_BE_DELETED_MESSAGE);
        }

        categoryRepository.delete(category);

        return categoryMapper.categoryToCategoryDTO(category);

    }

    public long getNumberOfCategories() {
        return categoryRepository.numberOfPublishedCategory();

    }

    public List<CategoryDTO> getAllCategories() {

//        Set<Role> userRoles = userService.getCurrentUser().getRoles();
        List<CategoryDTO> categoryDTOList = categoryMapper.categoryListToCategoryDTOList(categoryRepository.findAll());
        List<CategoryDTO> newCategoryDTOList = new ArrayList<>();

        return categoryDTOList;

//        boolean isAdmin = false;
//        boolean isProductManager = false;

//        for (Role role : userRoles) {
//            if (RoleType.ROLE_ADMIN.equals(role.getType())) {
//                isAdmin = true;
//
//            } else if (RoleType.ROLE_PRODUCT_MANAGER.equals(role.getType())) {
//                isProductManager = true;
//
//            }
//        }
//        if (isAdmin) {
//            return categoryDTOList;
//        }
//        if (isProductManager) {
//            for (CategoryDTO categoryDTO : categoryDTOList) {
//                if (categoryDTO.getIsActive()) {
//                    newCategoryDTOList.add(categoryDTO);
//                }
//            }
//            return newCategoryDTOList;
//        }
//        throw new ResourceNotFoundException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));
    }

    //B02
    public CategoryDTO getCategoryByID(Long id) {

//        Set<Role> userRole = userService.getCurrentUser().getRoles();
        Category category = getCategory(id);

        return categoryMapper.categoryToCategoryDTO(category);

//        boolean isAdmin = false;
//        boolean isProductManager = false;
//
//        for (Role role : userRole) {
//            if (RoleType.ROLE_ADMIN.equals(role.getType())) {
//                isAdmin = true;
//            }
//            else if (RoleType.ROLE_PRODUCT_MANAGER.equals(role.getType())) {
//                isProductManager = true;
//            }
//        }

//        if (isAdmin) {
//            return categoryMapper.categoryToCategoryDTO(category);
////            throw new ResourceNotFoundException(String.format(ErrorMessage.NO_ACTIVE_CATEGORY_MESSAGE));
//        }
//        if (isProductManager) {
//            if (!category.getIsActive()) {
//                throw new ResourceNotFoundException(String.format(ErrorMessage.NO_ACTIVE_CATEGORY_MESSAGE));
//            }
//            return categoryMapper.categoryToCategoryDTO(category);
//        }
//        throw new ResourceNotFoundException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));

    }
}
