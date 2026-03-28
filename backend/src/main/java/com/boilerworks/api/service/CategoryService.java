package com.boilerworks.api.service;

import com.boilerworks.api.dto.*;
import com.boilerworks.api.model.Category;
import com.boilerworks.api.repository.CategoryRepository;
import com.boilerworks.api.security.BoilerworksUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(String search) {
        List<Category> categories;
        if (search != null && !search.isBlank()) {
            categories = categoryRepository.search(search);
        } else {
            categories = categoryRepository.findAllByOrderBySortOrderAsc();
        }
        return categories.stream().map(CategoryDto::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(UUID id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return CategoryDto.from(category);
    }

    public Category create(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(SlugUtil.slugify(request.getName()));
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder());

        return categoryRepository.save(category);
    }

    public Category update(UUID id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (request.getName() != null) {
            category.setName(request.getName());
            category.setSlug(SlugUtil.slugify(request.getName()));
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }

        return categoryRepository.save(category);
    }

    public void softDelete(UUID id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        UUID userId = getCurrentUserId();
        category.softDelete(userId);
        categoryRepository.save(category);
    }

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof BoilerworksUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
}
