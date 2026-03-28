package com.boilerworks.api.controller;

import com.boilerworks.api.dto.*;
import com.boilerworks.api.model.Category;
import com.boilerworks.api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('category.view')")
    public List<CategoryDto> list(@RequestParam(required = false) String search) {
        return categoryService.findAll(search);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('category.view')")
    public CategoryDto get(@PathVariable UUID id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('category.add')")
    public ApiResponse<CategoryDto> create(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = categoryService.create(request);
        return ApiResponse.ok(CategoryDto.from(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('category.change')")
    public ApiResponse<CategoryDto> update(@PathVariable UUID id,
                                            @Valid @RequestBody UpdateCategoryRequest request) {
        Category category = categoryService.update(id, request);
        return ApiResponse.ok(CategoryDto.from(category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('category.delete')")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        categoryService.softDelete(id);
        return ApiResponse.ok(null);
    }
}
